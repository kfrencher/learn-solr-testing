package com.kfrencher;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

/**
 * Created by Kenneth on 6/19/2014.
 */
public class TestTokenizerInSolr extends AbstractSolrTestCase {
    private SolrServer server;

    @Override
    public String getSchemaFile() {
        return "schema.xml";
    }

    @Override
    public String getSolrConfigFile() {
        return "solrconfig.xml";
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        server = new EmbeddedSolrServer(h.getCoreContainer() , h.getCore().getName());
    }

    @Override
    @After
    public void tearDown() throws Exception {
        server.shutdown();
        super.tearDown();
    }

    public void testTokenizerInSolr() throws SolrServerException, IOException {
        ModifiableSolrParams params = new ModifiableSolrParams();

        // ** Let's index a document into our embedded server

        SolrInputDocument newDoc = new SolrInputDocument();
        newDoc.addField("title", "Test Document 1");
        newDoc.addField("id", "doc-1");
        newDoc.addField("text", "Hello world!");
        server.add(newDoc);
        server.commit();

        // ** And now let's query for it

        params.set("q", "title:test");
        QueryResponse qResp = server.query(params);

        SolrDocumentList docList = qResp.getResults();
        System.out.println("Num docs: " + docList.getNumFound());
        SolrDocument doc = docList.get(0);
        System.out.println("Title: " + doc.getFirstValue("title").toString());
    }
}
