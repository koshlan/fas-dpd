package fasdpd;

//import java.util.Arrays;
//import java.util.List;
//import java.util.Vector;

import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.FloatParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.StringParameter;

//import fasdpd.tokens.*;


import validator.ValidateDegeneratedEnd;
import validator.ValidateRepeatedEnd;
import validator.ValidateAlways;
import validator.Validate_AND;
import validator.Validate_NOT;
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
 * This class contains all the parameters search.
 * @author Javier Iserte <jiserte@unq.edu.ar>
 * @version 1.1.3
 */
public class SearchParameter {
	// parameters are stored as instance variables 
	private String infile;
	private String outfile;
	private String gcfile;
	private String profile="";
	private int len;
	private Validator filter= new ValidateAlways();
	private int quantity=20;
	private int startPoint=1; // The number of the first position is One, not Zero.
	private int endPoint=-1; // -1 represents the end the sequence.
	private boolean directStrand=true; 
	private boolean isDNA = false;
	private float Nx; 
	private float Ny;
	private float pA;
	
	


	// CONSTRUCTOR
	/**
	 * 
	 */
	public SearchParameter() {
		super();
	}

	// Public METHODS
	/**
	 * 
	 */
	public void retrieveFromCommandLine(String[] args) throws InvalidCommandLineException {
	
		Parser parser = new Parser();
		
		SingleOption len = new SingleOption(parser, 20 , "/len", IntegerParameter.getParameter());
		SingleOption infile = new SingleOption(parser, null , "/infile", StringParameter.getParameter());
		SingleOption outfile = new SingleOption(parser, null , "/outfile", StringParameter.getParameter());
		SingleOption gcfile = new SingleOption(parser, null , "/gcfile", StringParameter.getParameter());
		SingleOption profile = new SingleOption(parser, null , "/profile", StringParameter.getParameter());
		SingleOption quantity = new SingleOption(parser, 20 , "/q", IntegerParameter.getParameter());
		SingleOption start = new SingleOption(parser, 1 , "/startingpoint", IntegerParameter.getParameter());
		SingleOption end = new SingleOption(parser, -1 , "/endpoint", IntegerParameter.getParameter());

		SingleOption nx = new SingleOption(parser, (float)1 , "/nx", FloatParameter.getParameter());
		SingleOption ny = new SingleOption(parser, (float)1 , "/ny", FloatParameter.getParameter());
		SingleOption pa = new SingleOption(parser, (float)0 , "/pa", FloatParameter.getParameter());

		
		NoOption isDna = new NoOption(parser, true , "/isDNA");
		NoOption isProtein = new NoOption(parser, false , "/isProtein");
		
		NoOption filterRep = new NoOption(parser, false , "/frep");
		NoOption filterDeg = new NoOption(parser, false , "/fdeg");
		NoOption complementary = new NoOption(parser, false , "/ComplementaryStrand");
		
		
		
		
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			e.printStackTrace();
		}
		
		// Set Values
		if (! (infile.isPresent()&&outfile.isPresent()&&gcfile.isPresent())) {
			// infile, outfile and gcfile are required!
			// if one of them is not present then the command line is not well formed.
			throw new InvalidCommandLineException("Infile, Outfile and GCfile are required arguments");
		}
		if (infile.isPresent()) this.setInfile((String)infile.getValue());
		if (outfile.isPresent()) this.setOutfile((String)outfile.getValue());
		if (gcfile.isPresent()) this.setGCfile((String)gcfile.getValue());
			// pass the options of infile, outfile and gcfile
 
		this.setProfile((String)profile.getValue());
			// if profile is not present, the default value null is passed.
		
		this.setLen((Integer)len.getValue());
		this.setQuantity((Integer)quantity.getValue());
		this.setStartPoint((Integer) start.getValue());
		this.setEndPoint((Integer) end.getValue());
			// pass values for length, starting point, end point and number of primers
		
		this.setDirectStrand(! complementary.getValue());
			// pass if it is complementary.
		
		if (isDna.isPresent()&&isProtein.isPresent()) {
			// both option can not be in the command line at the same moment.
			throw new InvalidCommandLineException("isDna option and isProtein Option can not be in the command line at the same time.");
		}
		this.setDNA(isDna.getValue());
		if (isProtein.isPresent()) this.setDNA(isProtein.getValue());

		
		Validator v1 = new ValidateAlways();
		Validator v2 = new ValidateAlways();
		
		if ((Boolean) filterRep.getValue()) v1 = new Validate_NOT(new ValidateRepeatedEnd());
		if ((Boolean) filterDeg.getValue()) v1 = new Validate_NOT(new ValidateDegeneratedEnd());
		
		this.setFilter(new Validate_AND(v1, v2));
			// creates a filter and passes it.
		
		this.setNx( (Float) nx.getValue());
		this.setNy( (Float) ny.getValue());
		this.setpA( (Float) pa.getValue());
		
	}
	
	// GETTERS && SETTERS
	public String getInfile() {
		return infile;
	}
	public void setInfile(String infile) {
		this.infile = infile;
	}
	public String getOutfile() {
		return outfile;
	}
	public void setOutfile(String outfile) {
		this.outfile = outfile;
	}
	public String getGCfile() {
		return gcfile;
	}
	public void setGCfile(String gcfile) {
		this.gcfile = gcfile;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public Validator getFilter() {
		return filter;
	}
	public void setFilter(Validator filter) {
		this.filter = filter;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	/**
	 * The first index is One, not Zero.
	 */
	public int getStartPoint() {
		return startPoint;
	}
	/**
	 * The first index is One, not Zero.
	 * @param startPoint
	 */
	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}
	/**
	 * A value of -1 means the last position of the sequence
	 */
	public int getEndPoint() {
		return endPoint;
	}
	/**
	 * A value of -1 means the last position of the sequence
	 * @param endpoint
	 */
	public void setEndPoint(int endPoint) {
		this.endPoint = endPoint;
	}
	public boolean isDirectStrand() {
		return directStrand;
	}
	public void setDirectStrand(boolean directStrand) {
		this.directStrand = directStrand;
	}
	public boolean isDNA() {
		return  this.isDNA;
	}
	public void setDNA(boolean isDNA) {
		this.isDNA = isDNA;
	}
	public void setProfile(String profile) {
		this.profile= profile;
	}
	public String getProfile() {
		return this.profile;
	}
	
	public void setNx(float nx) {
		this.Nx = nx;
	}
	public float getNx() {
		return this.Nx;
	}

	public void setNy(float ny) {
		this.Ny = ny;
	}
	public float getNy() {
		return this.Ny;
	}
	
	public void setpA(float pA) {
		this.pA = pA;
	}
	public float getpA() {
		return this.pA;
	}

}
