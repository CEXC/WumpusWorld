package wumpusworld.solutions.ws1112.JTSBMMSSNR;

public enum AgentenZustand {
	FLUECHTEN {
		public String toString() {
			return "Fluechten";
		}
	},
	JAGEN {
		public String toString() {
			return "Jagen";
		}
	},
	GOLDSAMMELN {
		public String toString() {
			return "Gold sammeln";
		}
	}
}
