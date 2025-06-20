import { Component, ElementRef, EventEmitter, Output, QueryList, ViewChildren, HostListener, Input, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-verification-code',
  standalone: false,
  templateUrl: './verification-code.component.html',
  styleUrls: ['./verification-code.component.css']
})
export class VerificationCodeComponent {
  @ViewChildren('codeInput') codeInputs!: QueryList<ElementRef<HTMLInputElement>>;
  @Input() code: string = '';
  @Output() codeChange = new EventEmitter<string>();

  @Output() codeComplete = new EventEmitter<string>();
  @Output() codeChanged = new EventEmitter<string>();


  ngOnChanges(changes: SimpleChanges): void {
    if (changes['code']) {
      this.updateInputsFromCode(this.code);
    }
  }

  updateInputsFromCode(code: string) {
    if (!this.codeInputs || this.codeInputs.length === 0) {
      setTimeout(() => this.updateInputsFromCode(code), 50);
      return;
    }

    const codeArr = code ? code.split('') : [];
    this.codeInputs.forEach((input, index) => {
      input.nativeElement.value = codeArr[index] || '';
    });
  }

  setCode(code: string) {
    if (!this.codeInputs || !this.codeInputs.length) {
      // Retry after a delay when ViewChildren is populated
      setTimeout(() => this.setCode(code), 10);
      return;
    }

    const inputArray = this.codeInputs.toArray();
    code.split('').forEach((digit, index) => {
      if (inputArray[index]) {
        inputArray[index].nativeElement.value = digit;
      }
    });
  }
  verificationForm: FormGroup;
  readonly CODE_LENGTH = 6;

  constructor(private fb: FormBuilder) {
    this.verificationForm = this.fb.group({
      codes: this.fb.array(
        Array(this.CODE_LENGTH).fill('').map(() =>
          this.fb.control('', [Validators.required, Validators.pattern(/^\d$/)])
        )
      )
    });
  }

  get codesArray(): FormArray {
    return this.verificationForm.get('codes') as FormArray;
  }

  // Global paste event listener to catch Ctrl+V anywhere in the component
  @HostListener('paste', ['$event'])
  onGlobalPaste(event: ClipboardEvent): void {
    // Check if the paste event is happening within our component
    const target = event.target as HTMLElement;
    if (target && target.classList.contains('code-input')) {
      this.handlePaste(event);
    }
  }


  onInput(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    if (!this.codeInputs) return;
    const newCode = this.codeInputs
      .map(input => input.nativeElement.value)
      .join('');
    this.code = newCode;
    this.codeChange.emit(newCode);  // assuming you use `@Output() codeChange = new EventEmitter<string>();`
    // Only allow single digit
    if (value.length > 1) {
      input.value = value.charAt(value.length - 1);
      this.codesArray.at(index).setValue(input.value);
    }

    // Auto-focus next input
    if (input.value && index < this.CODE_LENGTH - 1) {
      this.focusInput(index + 1);
    }

    this.updateCode();
  }

  onKeyDown(event: KeyboardEvent, index: number): void {
    const input = event.target as HTMLInputElement;

    // Handle Ctrl+V specifically
    if (event.ctrlKey && event.key === 'v') {
      // Let the paste event handle this
      return;
    }

    // Handle backspace
    if (event.key === 'Backspace') {
      if (!input.value && index > 0) {
        // Move to previous input and clear it
        this.focusInput(index - 1);
        this.codesArray.at(index - 1).setValue('');
      } else if (input.value) {
        // Clear current input
        input.value = '';
        this.codesArray.at(index).setValue('');
      }
      this.updateCode();
      return;
    }

    // Handle delete key
    if (event.key === 'Delete') {
      input.value = '';
      this.codesArray.at(index).setValue('');
      this.updateCode();
      return;
    }

    // Handle arrow keys
    if (event.key === 'ArrowLeft' && index > 0) {
      this.focusInput(index - 1);
      return;
    }

    if (event.key === 'ArrowRight' && index < this.CODE_LENGTH - 1) {
      this.focusInput(index + 1);
      return;
    }

    // Only allow numeric input
    if (!/^\d$/.test(event.key) && !['Backspace', 'Delete', 'Tab', 'ArrowLeft', 'ArrowRight'].includes(event.key)) {
      event.preventDefault();
    }
  }

  onPaste(event: ClipboardEvent, index: number): void {
    this.handlePaste(event);
  }

  onContainerPaste(event: ClipboardEvent): void {
    this.handlePaste(event);
  }

  private handlePaste(event: ClipboardEvent): void {
    event.preventDefault();
    event.stopPropagation();

    // Try multiple ways to get clipboard data
    let pastedData = '';
    if (event.clipboardData) {
      pastedData = event.clipboardData.getData('text/plain') ||
                   event.clipboardData.getData('text') || '';
    }

    console.log('Pasted data:', pastedData); // Debug log

    // Extract only digits from pasted content
    const digits = pastedData.replace(/\D/g, '').split('');
    console.log('Extracted digits:', digits); // Debug log

    if (digits.length === 0) {
      console.log('No digits found in pasted content');
      return;
    }

    // Clear all inputs first
    this.clearAll();

    // Fill inputs with pasted digits, starting from the first input
    const digitsToFill = digits.slice(0, this.CODE_LENGTH);

    // Use a more reliable approach to update both form controls and DOM elements
    digitsToFill.forEach((digit, i) => {
      this.codesArray.at(i).setValue(digit);
    });

    // Update DOM elements after a short delay to ensure form controls are updated
    setTimeout(() => {
      digitsToFill.forEach((digit, i) => {
        const inputElement = this.codeInputs.toArray()[i]?.nativeElement;
        if (inputElement) {
          inputElement.value = digit;
        }
      });

      // Focus the next empty input or the last filled input
      const nextIndex = Math.min(digitsToFill.length, this.CODE_LENGTH - 1);
      this.focusInput(nextIndex);
      this.updateCode();
    }, 0);
  }

  onFocus(event: FocusEvent, index: number): void {
    const input = event.target as HTMLInputElement;
    input.select();
  }

  private focusInput(index: number): void {
    setTimeout(() => {
      const inputElement = this.codeInputs.toArray()[index]?.nativeElement;
      if (inputElement) {
        inputElement.focus();
      }
    }, 0);
  }

  private updateCode(): void {
    const code = this.codesArray.controls.map(control => control.value).join('');

    // Emit changes
    this.code = code;
    this.codeChange.emit(code);         // This updates the parent's bound variable
    this.codeChanged.emit(code);        // Optional if you want to catch intermediate changes

    if (code.length === this.CODE_LENGTH && this.codesArray.valid) {
      this.codeComplete.emit(code);     // Trigger parent’s auto-verify when code is full
    }
  }

  private clearAll(): void {
    this.codesArray.controls.forEach(control => control.setValue(''));
    this.codeInputs.forEach(input => {
      if (input.nativeElement) {
        input.nativeElement.value = '';
      }
    });
    this.updateCode();  // Make sure to update state
  }

  // Public methods for external control
  clear(): void {
    this.clearAll();
    this.focusInput(0);
    this.updateCode();
  }

  focus(): void {
    this.focusInput(0);
  }

  isValid(): boolean {
    return this.verificationForm.valid;
  }

  getCode(): string {
    return this.codesArray.value.join('');
  }
}
