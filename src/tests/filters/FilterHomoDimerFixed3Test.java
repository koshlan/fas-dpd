/*
 * You may not change or alter any portion of this comment or credits
 * of supporting developers from this source code or any supporting source code
 * which is considered copyrighted (c) material of the original comment or credit authors.
 *
 * THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. 
 * EXCEPT WHEN OTHERWISE STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES 
 * PROVIDE THE PROGRAM �AS IS� WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE 
 * PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL 
 * NECESSARY SERVICING, REPAIR OR CORRECTION.
 
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL ANY COPYRIGHT 
 * HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR CONVEYS THE PROGRAM AS PERMITTED ABOVE, 
 * BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL
 * DAMAGES ARISING OUT OF THE USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED 
 * TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD 
 * PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS), EVEN IF SUCH 
 * HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
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

package tests.filters;

import sequences.dna.Primer;
import sequences.util.compare.DegeneratedDNAMatchingStrategy;
import filters.singlePrimer.FilterHomoDimerFixed3;
import filters.singlePrimer.FilterSinglePrimer;
import junit.framework.TestCase;

public class FilterHomoDimerFixed3Test extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testValidate() {
		
		Primer p1a = new Primer("ACGT", "desc", 1, 20, true);
		Primer p2a = new Primer("ACTAG", "desc", 1, 20, true);
		Primer p3a = new Primer("CCTA", "desc", 1, 20, false);
		Primer p4a = new Primer("ACTG", "desc", 1, 20, false);
		Primer p5a = new Primer("ACTGT", "desc", 1, 20, false);
		Primer p6a = new Primer("ACCGG", "desc", 1, 20, false);
		Primer p7a = new Primer("CCGGA", "desc", 1, 20, false);
		
		FilterSinglePrimer filter2 = new FilterHomoDimerFixed3(2,new DegeneratedDNAMatchingStrategy());
		FilterSinglePrimer filter3 = new FilterHomoDimerFixed3(3,new DegeneratedDNAMatchingStrategy());
		FilterSinglePrimer filter4 = new FilterHomoDimerFixed3(4,new DegeneratedDNAMatchingStrategy());
		
		assertFalse(filter2.filter(p1a));
		assertFalse(filter2.filter(p2a));
		assertTrue(filter2.filter(p3a));
		assertTrue(filter2.filter(p4a));
		assertTrue(filter2.filter(p5a));
		assertFalse(filter2.filter(p6a));
		assertTrue(filter2.filter(p7a));
		
		assertFalse(filter3.filter(p1a));
		assertFalse(filter3.filter(p2a));
		assertTrue(filter3.filter(p3a));
		assertTrue(filter3.filter(p4a));
		assertTrue(filter3.filter(p5a));
		assertFalse(filter3.filter(p6a));
		assertTrue(filter3.filter(p7a));

		assertTrue(filter4.filter(p1a));
		assertTrue(filter4.filter(p2a));
		assertTrue(filter4.filter(p3a));
		assertTrue(filter4.filter(p4a));
		assertTrue(filter4.filter(p5a));
		assertTrue(filter4.filter(p6a));
		assertTrue(filter4.filter(p7a));
		
	}
}
