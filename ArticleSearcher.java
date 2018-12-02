import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import opennlp.tools.stemmer.PorterStemmer;

public class ArticleSearcher {
	
	// Set of document
	List<Document> docs = null;
	public static PorterStemmer porterStemmer = new PorterStemmer();
	public static final Set<String> stopWords = Stream.of("a","about","above","after","again","against","all","am","an","and","any","are","aren't","as","at","be","because","been","before","being","below","between","both","but","by","can't","cannot","could","couldn't","did","didn't","do","does","doesn't","doing","don't","down","during","each","few","for","from","further","had","hadn't","has","hasn't","have","haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","him","himself","his","how","how's","i","i'd","i'll","i'm","i've","if","in","into","is","isn't","it","it's","its","itself","let's","me","more","most","mustn't","my","myself","no","nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out","over","own","same","shan't","she","she'd","she'll","she's","should","shouldn't","so","some","such","than","that","that's","the","their","theirs","them","themselves","then","there","there's","these","they","they'd","they'll","they're","they've","this","those","through","to","too","under","until","up","very","was","wasn't","we","we'd","we'll","we're","we've","were","weren't","what","what's","when","when's","where","where's","which","while","who","who's","whom","why","why's","with","won't","would","wouldn't","you","you'd","you'll","you're","you've","your","yours","yourself","yourselves").collect(Collectors.toSet());
	private HashMap<String, HashSet<String>> conDocument;	//Remove duplicate of content token in the document
	private HashMap<String, HashSet<String>> titleDocument;	//Remove duplicate of titletoken in the document
	private HashMap<String, Document> docWID;				//Map docID with doc
	private HashSet<String> termPoolC;						//Keep all tokens of all docs
	private HashSet<String> termPoolT;						//Keep all tokens of all docs
	private HashMap<String, Double> idfContent;				//Map termName with value of idf
	private HashMap<String, Double> idfTitle;				//Map termName with value of idf
	private HashMap<String, Double> rankC;					//Map score with docID
	private HashMap<String, Double> rankT;					//Map score with docID
	private HashMap<String, Double> conMagnitude;			//Map docID with their magnitude
	private HashMap<String, Double> titleMagnitude;			//Map docID with their magnitude
	private HashMap<String, Integer> debugC;
	private HashMap<String, Integer> debugT;
	
	 ArticleSearcher(File file) throws IOException {
		this.docs = parseDocumentFromFile(file);
		conDocument = new HashMap<String, HashSet<String>>();
		titleDocument = new HashMap<String, HashSet<String>>();
		termPoolC = new HashSet<String>();
		termPoolT = new HashSet<String>();
		idfContent = new HashMap<String, Double>();
		idfTitle = new HashMap<String, Double>();
		rankC = new HashMap<String, Double>();
		rankT = new HashMap<String, Double>();
		conMagnitude = new HashMap<String, Double>();
		titleMagnitude = new HashMap<String, Double>();
		
		debugC = new HashMap<String, Integer>();
		debugT = new HashMap<String, Integer>();
		docWID = new HashMap<String, Document>();

		
		// Remove duplicate of document, and Map it in newDocument, termPool is finish
		for (Document doc : this.docs) {
			HashSet<String> tempSet = new HashSet<String>();
			tempSet.addAll(doc.getContentTokens());
			termPoolC.addAll(doc.getContentTokens());	//Add token into TermPool Content
			termPoolT.addAll(doc.getTitleTokens());		//Add token into TermPool Title
			conDocument.put(doc.getA_id(), tempSet);
			titleDocument.put(doc.getA_id(), tempSet);
			docWID.put(doc.getA_id(), doc);
		}
		
		// Calculate idf of each Token (Less Accuracy, but spend less time) content
		for (Map.Entry<String, HashSet<String>> entry : conDocument.entrySet()) {
			for (String str : entry.getValue()) {
				if(!debugC.keySet().contains(str))
					debugC.put(str, 1);
				else {
					int num = debugC.get(str);
					num++;
					debugC.put(str, num);
				}
			}
		}
		
		// Calculate idf of each Token (Less Accuracy, but spend less time) title
			for (Map.Entry<String, HashSet<String>> entry : titleDocument.entrySet()) {
				for (String str : entry.getValue()) {
					if(!debugT.keySet().contains(str))
						debugT.put(str, 1);
					else {
						int num = debugT.get(str);
						num++;
						debugT.put(str, num);
					}
				}
			}
		
		//content
		for (Map.Entry<String, Integer> entry : debugC.entrySet()) {
			int count = entry.getValue();
			double tempIDF = Math.log10(1 + (this.docs.size() / (double)count));
			idfContent.put(entry.getKey(), tempIDF);
		}
		
		//title
		for (Map.Entry<String, Integer> entry : debugT.entrySet()) {
			int count = entry.getValue();
			double tempIDF = Math.log10(1 + (this.docs.size() / (double)count));
			idfTitle.put(entry.getKey(), tempIDF);
		}
		
		//Calculate Magnitude of content in each doc
		for (Map.Entry<String, HashSet<String>> entry : conDocument.entrySet()) {
			
			double sum = 0.0;
			
			for (String str : entry.getValue()) {
				int freq = Collections.frequency(docWID.get(entry.getKey()).getContentTokens(), str);
				double tempIDF = idfContent.get(str);
				double tempTF = 1.0 + Math.log10(freq);
				double tempTFIDF = tempTF * tempIDF;
				double square = Math.pow(tempTFIDF, 2);
				
				sum += square;
			}
			conMagnitude.put(entry.getKey(), Math.sqrt(sum));
		}

		//Calculate Magnitude of title of each doc
		for (Map.Entry<String, HashSet<String>> entry : titleDocument.entrySet()) {
			
			double sum = 0.0;
			
			for (String str : entry.getValue()) {
				int freq = Collections.frequency(docWID.get(entry.getKey()).getContentTokens(), str);
				double tempIDF = idfTitle.get(str);
				double tempTF = 1.0 + Math.log10(freq);
				double tempTFIDF = tempTF * tempIDF;
				double square = Math.pow(tempTFIDF, 2);
				
				sum += square;
			}
			titleMagnitude.put(entry.getKey(), Math.sqrt(sum));
		}
	}
	
	public static List<Document> parseDocumentFromFile(File file){
		List<Document> documents = new Vector<Document>();
		for(File fileEntry : file.listFiles()) {
			//add a document entry to documents
			Document doc = new Document(fileEntry);
			doc.setContentTokens(tokenize(doc.getContent()));
			doc.setTitleTokens(tokenize(doc.getA_title()));
			documents.add(doc);
		}
				
		return documents;
	}
	
	public static List<String> tokenize(String rawText)
	{
		//lower casing
		String text = rawText.toLowerCase();
		
		//remove noise
		text = text.replaceAll("[^a-zA-Z0-9]", " ");
		
		//tokenizing
		String[] tokenArray = text.split("\\s+");
		
		//stemming, cleaning individual characters, and removing stop words
		List<String> tokens = new Vector<String>();
		for(String t: tokenArray)
		{
			if(t.length() <= 1) continue;
			if(stopWords.contains(t)) continue;
			 
//			t = porterStemmer.stem(t);
			tokens.add(t);
		}
		//return
		return tokens;
	}
	
	public List<SearchResult> search(String queryString, int k) {
		/************* YOUR CODE HERE ******************/
		//Create token of Query
		List<SearchResult> ret = new Vector<SearchResult>();
		List<String> tokens = tokenize(queryString); //Tokenize the Query
		List<String> inQC = new Vector<String>(); //Keep token that be in Query
		List<String> inQT = new Vector<String>(); //Keep token that be in Query
		
		Vector<Double> vectorQC = new Vector<Double>();
		Vector<Double> vectorQT = new Vector<Double>();
		double magnitudeQC, magnitudeQT;
		
		//Calculate tfidf of Query Content
		for (String term : termPoolC) {
			int tf = Collections.frequency(tokens, term);
			if(tf != 0) {
				vectorQC.add(idfContent.get(term) * (1 + Math.log10(tf)));
				inQC.add(term);
			}
		}
		
		//Calculate tfidf of Query Title
		for (String term : termPoolT) {
			int tf = Collections.frequency(tokens, term);
			if(tf != 0) {
				vectorQT.add(idfContent.get(term) * (1 + Math.log10(tf)));
				inQT.add(term);
			}
		}
		
		//Calculate magnitude of Query Content
		double sum = 0.0;
		for (Double num : vectorQC) {
			sum += Math.pow(num, 2);
		}
		magnitudeQC = Math.sqrt(sum);

		//Calculate magnitude of Query Title
		sum = 0.0;
		for (Double num : vectorQT) {
			sum += Math.pow(num, 2);
		}
		magnitudeQT = Math.sqrt(sum);

		
		//calculate tfidf of each doc
		for (Document doc : this.docs) {
			Vector<Double> vectorDocC = new Vector<Double>();
			Vector<Double> vectorDocT = new Vector<Double>();

			for (int i = 0; i < inQC.size(); i++) {
				int tf = Collections.frequency(doc.getContentTokens(), inQC.get(i));
				if(tf == 0)
					vectorDocC.add(0.0);
				else
					vectorDocC.add(idfContent.get(inQC.get(i)) * (1 + Math.log10(tf)));
			}
			
			for (int i = 0; i < inQT.size(); i++) {
				int tf = Collections.frequency(doc.getContentTokens(), inQT.get(i));
				if(tf == 0)
					vectorDocT.add(0.0);
				else
					vectorDocT.add(idfTitle.get(inQT.get(i)) * (1 + Math.log10(tf)));
			}
			
			//Calculate rank Content
			double top = 0.0, bottom;
			for (int i = 0; i < inQC.size(); i++)
				top += vectorQC.get(i) * vectorDocC.get(i);
			bottom = magnitudeQC * conMagnitude.get(doc.getA_id());
			
			if(bottom == 0)
				rankC.put(doc.getA_id(), -1.0);
			else
				rankC.put(doc.getA_id(), top / bottom);
			
			//Calculate rank Title
			top = 0.0; 
			for (int i = 0; i < inQT.size(); i++)
				top += vectorQT.get(i) * vectorDocT.get(i);
			bottom = magnitudeQT * titleMagnitude.get(doc.getA_id());
			
			if(bottom == 0)
				rankT.put(doc.getA_id(), -1.0);
			else
				rankT.put(doc.getA_id(), top / bottom);
			
			
			//Release Memory
			vectorDocC.clear();
			vectorDocT.clear();			
		}
		
		// Find the first k-doc
		String[] kDoc = new String[k];
		int pointer = 0, checkCT = 0; 
		String docC = "", docT = "";
		
		//check rank of content & title (should be equal)
		System.out.println(rankC.size() + " " + rankT.size());
		
		while(pointer != k) {
			double maxC = Integer.MIN_VALUE;
			double maxT = Integer.MIN_VALUE;
			docC = "";
			for (Map.Entry<String, Double> entry : rankC.entrySet()) {
				boolean check = true;
				for (int i = 0; i < pointer; i++)
					if(entry.getKey() == kDoc[i]) check = false;
				if(check) {
					if(maxC < entry.getValue()) {
						maxC = entry.getValue();
						docC = entry.getKey();
					}
				}
			}
			docT = "";
			for (Map.Entry<String, Double> entry : rankT.entrySet()) {
				boolean check = true;
				for (int i = 0; i < pointer; i++)
					if(entry.getKey() == kDoc[i]) check = false;
				if(check) {
					if(maxT < entry.getValue()) {
						maxT = entry.getValue();
						docT = entry.getKey();
					}
				}
			}
			if(rankC.get(docC) > rankT.get(docT)) {
				kDoc[pointer] = docC;
				SearchResult tempRet = null;
				if (rankC.get(docC) < 0) {
					double temp = 0.0/0.0;
					tempRet = new SearchResult(docWID.get(docC), temp);
				}
				else {
					if(rankC.get(docC) > 1)
						tempRet = new SearchResult(docWID.get(docC), 1);
					else
						tempRet = new SearchResult(docWID.get(docC), rankC.get(docC));
				}
				ret.add(tempRet);
				checkCT = 1;
			}
			else {
				kDoc[pointer] = docT;
				SearchResult tempRet = null;
				if (rankT.get(docT) == -1) {
					double temp = 0.0/0.0;
					tempRet = new SearchResult(docWID.get(docT), temp);
				}
				else {
					if(rankT.get(docT) > 1)
						tempRet = new SearchResult(docWID.get(docT), 1);
					else
						tempRet = new SearchResult(docWID.get(docT), rankT.get(docT));
				}
				ret.add(tempRet);
				checkCT = 2;
			}

			pointer++;
		}
		
		return ret;
		/***********************************************/
	}

	
	public static void displaySearchResults(List<SearchResult> results)
	{	StringBuilder str = new StringBuilder();
		for(int i = 0; i < results.size(); i++)
		{
			str.append("<"+(i+1)+">"+results.get(i));
		}
		System.out.println(str);
	}
}
