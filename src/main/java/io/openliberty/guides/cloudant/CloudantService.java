package io.openliberty.guides.cloudant;

import com.ibm.cloud.cloudant.v1.Cloudant;
import com.ibm.cloud.cloudant.v1.model.*;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import io.openliberty.guides.cos.model.DbData;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

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

    private Cloudant cdClient;

    public CloudantService(){
        IamAuthenticator authenticator = new IamAuthenticator(apikey);
        cdClient = new Cloudant(Cloudant.DEFAULT_SERVICE_NAME, authenticator);
        cdClient.setServiceUrl(url);
    }

    public DocumentResult createDocument(DbData data){
        Document eventDoc = new Document();
        eventDoc.put("title", data.getTitle());
        eventDoc.put("cosObjectName", data.getCosObjectName());
        eventDoc.put("cosBucketName", data.getCosBucketName());
        eventDoc.put("status", data.getStatus());
        PutDocumentOptions documentOptions =
                new PutDocumentOptions.Builder()
                        .db(db)
                        .document(eventDoc)
                        .build();
        DocumentResult response =
                cdClient.putDocument(documentOptions).execute()
                        .getResult();
        return response;
    }

    public AllDocsResult getDocumentsList() {
        PostAllDocsOptions docsOptions =
                new PostAllDocsOptions.Builder()
                        .db(db)
                        .includeDocs(true)
                        .limit(10)
                        .build();

        AllDocsResult response =
                cdClient.postAllDocs(docsOptions).execute().getResult();
        return  response;
            }
}
