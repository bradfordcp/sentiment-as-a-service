package io.bradfordcp.saas;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import javax.enterprise.context.ApplicationScoped;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ApplicationScoped
public class SentimentService {
    StanfordCoreNLP tokenizer;
    StanfordCoreNLP pipeline;

    public SentimentService(){
        // We construct two pipelines.  One handles tokenization, if
        // necessary.  The other takes tokenized sentences and converts
        // them to sentiment trees.
        Properties pipelineProps = new Properties();
        Properties tokenizerProps = null;

        tokenizerProps = new Properties();
        tokenizerProps.setProperty("annotators", "tokenize, ssplit");

        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("parse.binaryTrees", "true");
        pipelineProps.setProperty("parse.buildgraphs", "false");
        pipelineProps.setProperty("enforceRequirements", "false");

        tokenizer = new StanfordCoreNLP(tokenizerProps);
        pipeline = new StanfordCoreNLP(pipelineProps);
    }

    private static final NumberFormat NF = new DecimalFormat("0.0000");


    public Float calculate(String line) {
        List<String> sentenceList = new ArrayList<>();

        line = line.trim();
        if ( ! line.isEmpty()) {
            Annotation annotation = new Annotation(line);
            tokenizer.annotate(annotation);
            pipeline.annotate(annotation);

            List<Integer> sentiments = new ArrayList<>();
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                sentiments.add(RNNCoreAnnotations.getPredictedClass(tree));
            }

            return (float) sentiments.stream().reduce(0, Integer::sum) / (float) sentiments.size();
        }

        return -1.0f;
    }
}
