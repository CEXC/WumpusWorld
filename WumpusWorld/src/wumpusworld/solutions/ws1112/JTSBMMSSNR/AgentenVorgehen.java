package wumpusworld.solutions.ws1112.JTSBMMSSNR;

public enum AgentenVorgehen {
	BREITENSUCHE {
		public String toString() {
			return "BS";
		}
	},
	UNIFORMEKOSTENSUCHE {
		public String toString() {
			return "UK";
		}
	},
	ASTERN {
		public String toString() {
			return "AS";
		}
	},
	ASTERNSPEZIAL {
		public String toString() {
			return "ASS";
		}
	};
	
	
}
