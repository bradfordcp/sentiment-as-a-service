package io.bradfordcp.saas;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import javax.enterprise.context.ApplicationScoped;
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

        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("parse.binaryTrees", "true");
        pipelineProps.setProperty("parse.buildgraphs", "false");
        pipelineProps.setProperty("enforceRequirements", "false");

        tokenizerProps = new Properties();
        tokenizerProps.setProperty("annotators", "tokenize, ssplit");

        tokenizerProps.setProperty(StanfordCoreNLP.NEWLINE_SPLITTER_PROPERTY, "true");

        tokenizer = new StanfordCoreNLP(tokenizerProps);
        pipeline = new StanfordCoreNLP(pipelineProps);
    }

    public Float calculate(String line) {
        line = line.trim();
        if ( ! line.isEmpty()) {
            Annotation annotation = tokenizer.process(line);
            pipeline.annotate(annotation);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                System.out.println(sentence.get(SentimentCoreAnnotations.SentimentClass.class));

                // FIXME right now only the first score is returned instead of an average
                return Float.parseFloat(sentence.get(SentimentCoreAnnotations.SentimentClass.class));
            }
        }

        return -1.0f;
    }
}
