package org.fnlp.app.lucene.demo;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.fnlp.app.lucene.FNLPAnalyzer;

import edu.fudan.nlp.cn.CNFactory;
import edu.fudan.nlp.cn.CNFactory.Models;
import edu.fudan.util.exception.LoadModelException;

public class Search {
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws LoadModelException 
	 */
	public static void main(String[] args) throws IOException, ParseException, LoadModelException {
		String indexPath = "./tmp/faqidx";
		System.out.println("Index directory '" + indexPath);
		Date start = new Date();
		Directory dir = FSDirectory.open(new File(indexPath));
		//需要先初始化 CNFactory
		CNFactory factory = CNFactory.getInstance("./models",Models.SEG_TAG);
		Analyzer analyzer = new FNLPAnalyzer(Version.LUCENE_40);
		// Now search the index:
		DirectoryReader ireader = DirectoryReader.open(dir);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		// Parse a simple query that searches for "text":
		QueryParser parser = new QueryParser(Version.LUCENE_40, "content", analyzer);
		Query query = parser.parse("终端 费用^4");
		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;

		// Iterate through the results:
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			System.out.println(hitDoc.get("content"));
			System.out.println(hits[i].score);
		}
		ireader.close();
		dir.close();
	}
}
