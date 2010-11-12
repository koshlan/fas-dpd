package fasdpd;

import degeneration.GeneticCode;
import sequences.DNASeq;
import sequences.Primer;
import validator.Validator;

/*
 * You may not change or alter any portion of this comment or credits
 * of supporting developers from this source code or any supporting source code
 * which is considered copyrighted (c) material of the original comment or credit authors.
 * This program is distributed WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * FAS-DPD project, including algorithms design, software implementation and experimental laboratory work, is being developed as a part of the Research Program:
 * 	"Microbiolog�a molecular b�sica y aplicaciones biotecnol�gicas"
 * 		(Basic Molecular Microbiology and biotechnological applications)
 * 
 * And is being conducted in:
 * 	LIGBCM: Laboratorio de Ingenier�a Gen�tica y Biolog�a Celular y Molecular.
 *		(Laboratory of Genetic Engineering and Cellular and Molecular Biology)
 *	Universidad Nacional de Quilmes.
 *		(National University Of Quilmes)
 *	Quilmes, Buenos Aires, Argentina.
 *
 * The complete team for this project is formed by:
 *	Lic.  Javier A. Iserte.
 *	Lic.  Betina I. Stephan.
 * 	ph.D. Sandra E. Go�i.
 * 	ph.D. P. Daniel Ghiringhelli.
 *	ph.D. Mario E. Lozano.
 *
 * Corresponding Authors:
 *	Javier A. Iserte. <jiserte@unq.edu.ar>
 *	Mario E. Lozano. <mlozano@unq.edu.ar>
 */
/**
 * This class computes scores and search the primers in a sequence.
 * @author Javier Iserte <jiserte@unq.edu.ar>
 * @version 1.3.3 
 */
public class Analyzer {
	private float pA;
	private float Ny;
	private float Nx;
	private GeneticCode gc;

	// CONSTRUCTOR
	/**
	 * Creates a new instance of Analyzer with standard parameters.
	 * 
	 */
	public Analyzer(GeneticCode myGeneticCode) {
		this.Nx= 1;
		this.Ny= 1;
		this.pA= 0;
		this.gc = myGeneticCode;
	}
	/**
	 * Creates a new instance of Analyzer with custom parameters.
	 * 
	 */
	public Analyzer(float pA, float Ny, float Nx, GeneticCode myGeneticCode) {
		this.Nx= Nx;
		this.Ny= Ny;
		this.pA= pA;
		this.gc = myGeneticCode;
	}
	
	
	// PUBLIC METHODS
	/**
	 * Search primers that have higher scores.
	 * 
	 * @param numberOfPrimers is the number of primers that are stored in the result.
	 * @param DNASeq is the sequence where the primers will be searched.
	 * @param primerLength is the length of desired primers.
	 * @param directStrand indicates that the sequence of primers will be search in the complementary strand of DNASeq.
	 * @param Filter is a Validator that checks if the primers meet any additional requirements.
	 * @StartPoint is the position of DNAseq where the search start.
	 * @EndPoint is the position of DNAseq where the search Ends. If EndPoint == -1 the search will be done until the end of the sequence is reached 
	 */
	public PriorityList<Primer> searchBestPrimers(int numberOfPrimers, DNASeq seq, int primerLength, boolean directStrand, Validator Filter, int StartPoint, int EndPoint) {
		// PRECONDTITION : StartPoint is equal o greater than one and is lesser than (sequence length - primer length)
		//               : End point is greater than startpoint plus primer length and lesser than sequence length. 
		PriorityList<Primer> lp = new PriorityList<Primer>(numberOfPrimers);
		
		DNASeq mySeq=null;
		
		if (directStrand) {mySeq = seq;} else {mySeq=(DNASeq) seq.getComplementary();}
		
		if (EndPoint==-1) {
			// EndPoint == -1 means that the end of the search coincides with the end of the sequence
			EndPoint = seq.getLength();
		}  
		
		for (int x=StartPoint-1; x<EndPoint-primerLength+1; x++) {
			Primer p = mySeq.designPrimer(x+1, x+primerLength, directStrand);
			
			if (Filter.validate(p)) {
				p.setScore(this.calculatePrimerScore(p));
				lp.addValue(p);
			}
			
		}
		
		return lp;
	}
	
	/**
	 *  calculatePrimerScore Calculates a primer score according to:	
	 *  <blockquote>
	 * 	Eq. 1: Sp_i = 1 - Log_10(NDi)<br>
	 * 	Eq. 2: Wp_i = pA + i * (Ny - pA) / Nx<br> 
	 *	Eq. 3: Ms = n * pA + (n + 1) * n * (Ny - pA) / ( 2 * Nx)<br>
	 *	Eq. 4: Sg = Suma(i=1,n,Sp * Wp) / Ms<br>
     *  </blockquote>
     * 
     *   @return the score for a primer.
	 */
	public float calculatePrimerScore(Primer p) {

		float sum =0;
		for (int i=0;i<p.getLength();i++) {
			char b = p.getSequence().charAt(i);
			sum = sum + this.Sp(gc.calculateDegValue(b))*this.Wp(i+1);
		}
		return sum/this.Ms(p.getLength());
		
	}
	
	// PRIVATE AND PROTECTED METHODS
	private float Sp(int NDi) {
		return (float) (1 - Math.log10(NDi));
	}
	
	private float Wp(int i) {
		return this.pA + i * (this.Ny - this.pA) / this.Nx;
	}
	
	private float Ms(int n) {
		return n * this.pA + (n + 1) * n * (this.Ny - this.pA) / ( 2 * this.Nx);
	}
}
