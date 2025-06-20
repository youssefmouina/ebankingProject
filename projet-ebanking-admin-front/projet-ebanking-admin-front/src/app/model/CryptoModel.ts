export interface CryptoModel {
  symbol: string;
  price: string;
  change: string;
  percent: string;
  volume: string;
  high24h?: string;
  low24h?: string;
  openPrice?: string;
  logoUrl?:string;
  quoteVolume?: string;
  marketCap?: string;
  rank?: string;
  currentPrice?: string;
  circulatingSupply?: string;
  totalVolume?: string;
  ath?: string;
  athDate?: string;
  lowestPriceMonth?: string; // Prix le plus bas sur 1 mois
  lowestPriceYear?: string;  // Prix le plus bas sur 1 an
  roiMonth?: string;         // Retour sur investissement sur 1 mois
  roiYear?: string;          // Retour sur investissement sur 1 an
}