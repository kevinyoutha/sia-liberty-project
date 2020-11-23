package com.keyou.test.cloudant;

import com.ibm.cloud.cloudant.v1.Cloudant;
import com.ibm.cloud.cloudant.v1.model.*;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.keyou.test.model.DbData;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class CloudantService {

    @Inject
    @ConfigProperty(name="cloudant_apikey")
    private String apikey;

    @Inject
    @ConfigProperty(name="cloudant_url")
    private String url;

    @Inject
    @ConfigProperty(name="cloudant_db")
    private String db;

//    private String url="https://26ba8f16-fee1-437b-8dd5-569d2c051252-bluemix:bec0fb6003500cc895cd88ca3f1f471e506fa211515f0541ae27c7c65ef1134c@26ba8f16-fee1-437b-8dd5-569d2c051252-bluemix.cloudantnosqldb.appdomain.cloud";
//    private String apikey="kh5H6jT0yzNwRv2wPn7656f_gA0LNMv9YG_kXrNn9akj";
//    private String db="mydb";
    private Cloudant cdClient;

    public CloudantService(){
        IamAuthenticator authenticator = new IamAuthenticator(apikey);
        cdClient = new Cloudant(Cloudant.DEFAULT_SERVICE_NAME, authenticator);
        cdClient.setServiceUrl(url);
    }

    public String createDocument(DbData data){
        Document exampleDocument = new Document();
        String exampleDocId = data.getCosObjectName();
        exampleDocument.setId(exampleDocId);
        exampleDocument.put("title", data.getTitle());
        exampleDocument.put("cosObjectName", data.getCosObjectName());
        exampleDocument.put("cosBucketName", data.getCosBucketName());
        exampleDocument.put("status", data.getStatus());

        // Set the options to get the document out of the database if it exists
        GetDocumentOptions documentInfoOptions =
                new GetDocumentOptions.Builder()
                        .db(db)
                        .docId(exampleDocId)
                        .build();

        /* Try to get the document and set revision of exampleDocument to the
           latest one if it previously existed in the database */
        try {
            Document documentInfo = cdClient
                    .getDocument(documentInfoOptions)
                    .execute()
                    .getResult();
            exampleDocument.setRev(documentInfo.getRev());
            System.out.println("The document revision for " + exampleDocId +
                    " is set to " + exampleDocument.getRev() + ".");
        } catch (Exception nfe) {
        }finally {
            // Save the document in the database
            PostDocumentOptions createDocumentOptions =
                    new PostDocumentOptions.Builder()
                            .db(db)
                            .document(exampleDocument)
                            .build();
            DocumentResult createDocumentResponse = cdClient
                    .postDocument(createDocumentOptions)
                    .execute()
                    .getResult();
            // Keep track of the revision number from the `example` document object
            exampleDocument.setRev(createDocumentResponse.getRev());
        }

        return  exampleDocument.toString();

    }

    public String getDocumentsList() {
        PostAllDocsOptions docsOptions =
                new PostAllDocsOptions.Builder()
                        .db(db)
                        .includeDocs(true)
                        .limit(50)
                        .build();
        AllDocsResult data =
                cdClient.postAllDocs(docsOptions).execute().getResult();
        List<Document> response =data.getRows().stream().map(DocsResultRow::getDoc).collect(Collectors.toList());
        return  response.toString();
    }
}
