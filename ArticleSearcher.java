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
	private HashMap<String, HashSet<String>> newDocument;	//Remove duplicate of token in the document
	private HashMap<String, Document> docWID;				//Map docID with doc
	private HashSet<String> termPool;						//Keep all tokens of all docs
	private HashMap<String, Double> idf;					//Map termName with value of idf
	private HashMap<String, Double> rank;					//Map score with docID
	private HashMap<String, Double> docMagnitude;			//Map docID with their magnitude
	private HashMap<String, Integer> debug;
	
	 ArticleSearcher(File file) throws IOException {
		this.docs = parseDocumentFromFile(file);
		newDocument = new HashMap<String, HashSet<String>>();
		termPool = new HashSet<String>();
		idf = new HashMap<String, Double>();
		rank = new HashMap<String, Double>();
		docMagnitude = new HashMap<String, Double>();
		
		debug = new HashMap<String, Integer>();
		docWID = new HashMap<String, Document>();
		
		// Remove duplicate of document, and Map it in newDocument, termPool is finish
		for (Document doc : this.docs) {
			HashSet<String> tempSet = new HashSet<String>();
			tempSet.addAll(doc.getTokens());
			termPool.addAll(doc.getTokens());	//Add token into TermPool
			newDocument.put(doc.getA_id(), tempSet);
			
			docWID.put(doc.getA_id(), doc);
		}
		
		// Calculate idf of each Token (Less Accuracy, but spend less time)
		for (Map.Entry<String, HashSet<String>> entry : newDocument.entrySet()) {
			for (String str : entry.getValue()) {
				if(!debug.keySet().contains(str))
					debug.put(str, 1);
				else {
					int num = debug.get(str);
					num++;
					debug.put(str, num);
				}
			}
		}
		
		for (Map.Entry<String, Integer> entry : debug.entrySet()) {
			int count = entry.getValue();
			double tempIDF = Math.log10(1 + (this.docs.size() / (double)count));
			idf.put(entry.getKey(), tempIDF);
		}
		
		//Calculate Magnitude of each doc
		for (Map.Entry<String, HashSet<String>> entry : newDocument.entrySet()) {
			
			double sum = 0.0;
			
			for (String str : entry.getValue()) {
				int freq = Collections.frequency(docWID.get(entry.getKey()).getTokens(), str);
				double tempIDF = idf.get(str);
				double tempTF = 1.0 + Math.log10(freq);
				double tempTFIDF = tempTF * tempIDF;
				double square = Math.pow(tempTFIDF, 2);
				
				sum += square;
			}
			docMagnitude.put(entry.getKey(), Math.sqrt(sum));
		}
	}
	
	public static List<Document> parseDocumentFromFile(File file){
		int i = 0;
		List<Document> documents = new Vector<Document>();
		for(File fileEntry : file.listFiles()) {
			//add a document entry to documents
			Document doc = new Document(fileEntry);
			List<String> tokens = tokenize(doc.getContent());
			doc.setTokens(tokens);
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
		List<String> inQ = new Vector<String>(); //Keep token that be in Query
		
		Vector<Double> vectorQ = new Vector<Double>();
		double magnitudeQ;
		
		//Calculate tfidf of Query
		for (String term : termPool) {
			int tf = Collections.frequency(tokens, term);
			if(tf != 0) {
				vectorQ.add(idf.get(term) * (1 + Math.log10(tf)));
				inQ.add(term);
			}
		}
		
		//Calculate magnitude of Query
		double sum = 0.0;
		for (Double num : vectorQ) {
			sum += Math.pow(num, 2);
		}
		magnitudeQ = Math.sqrt(sum);
		
		//calculate tfidf of each doc
		for (Document doc : this.docs) {
			Vector<Double> vectorDoc = new Vector<Double>();

			for (int i = 0; i < inQ.size(); i++) {
				int tf = Collections.frequency(doc.getTokens(), inQ.get(i));
				if(tf == 0)
					vectorDoc.add(0.0);
				else
					vectorDoc.add(idf.get(inQ.get(i)) * (1 + Math.log10(tf)));
			}
			
			//Calculate rank
			double top = 0.0, bottom;
			for (int i = 0; i < inQ.size(); i++)
				top += vectorQ.get(i) * vectorDoc.get(i);
			bottom = magnitudeQ * docMagnitude.get(doc.getA_id());
			
			if(bottom == 0)
				rank.put(doc.getA_id(), -1.0);
			else
				rank.put(doc.getA_id(), top / bottom);
			
			//Release Memory
			vectorDoc.clear();
			
		}
		
		// Find the first k-doc
		String[] kDoc = new String[k];
		int pointer = 0;
		
		while(pointer != k) {
			double max = Integer.MIN_VALUE;
			String doc = "";
			
			for (Map.Entry<String, Double> entry : rank.entrySet()) {
				boolean check = true;
				for (int i = 0; i < pointer; i++)
					if(entry.getKey() == kDoc[i]) check = false;
				if(check) {
					if(max < entry.getValue()) {
						max = entry.getValue();
						doc = entry.getKey();
					}
				}
			}
			kDoc[pointer] = doc;
			pointer++;
		}
		
		for (int i = 0; i < kDoc.length; i++) {
			SearchResult tempRet = null;
			if (rank.get(kDoc[i]) == -1) {
				double temp = 0.0/0.0;
				tempRet = new SearchResult(docWID.get(kDoc[i]), temp);
			}
			else {
				if(rank.get(kDoc[i]) > 1)
					tempRet = new SearchResult(docWID.get(kDoc[i]), 1);
				else
					tempRet = new SearchResult(docWID.get(kDoc[i]), rank.get(kDoc[i]));
			}
			ret.add(tempRet);
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
