/******************************************************************************
 * Copyright (c) 2018 IBM Corp.                                               *
 *                                                                            *
 * Licensed under the Apache License, Version 2.0 (the "License");            *
 * you may not use this file except in compliance with the License.           *
 * You may obtain a copy of the License at                                    *
 *                                                                            *
 *    http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                            *
 * Unless required by applicable law or agreed to in writing, software        *
 * distributed under the License is distributed on an "AS IS" BASIS,          *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 * See the License for the specific language governing permissions and        *
 * limitations under the License.                                             *
 ******************************************************************************/
package cloudant.rest;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import cloudant.Document;
import cloudant.store.DocumentStore;
import cloudant.store.DocumentStoreFactory;

@RequestScoped
@Path("/")
public class DocumentAPI {

    //Our database store
    DocumentStore store = DocumentStoreFactory.getInstance();

    /**
     * Gets all Documents.
     * REST API example:
     * <code>
     * GET http://localhost:9080/cloudant
     * </code>
     * 
     * Response:
     * <code>
     * [ "Bob", "Jane" ]
     * </code>
     * @return A collection of all the Documents
     */
    @GET
    @Produces({"application/json"})
    public String getDocuments() {

        if (store == null) {
            return "[]";
        }

        List<String> names = new ArrayList<String>();
        for (Document doc : store.getAll()) {
            String name = doc.getTitle();
            if (name != null){
                names.add(name);
            }
        }
        return new Gson().toJson(names);
    }
    
    /**
     * Creates a new Visitor.
     * 
     * REST API example:
     * <code>
     * POST http://localhost:9080/cloudant
     * <code>
     * POST Body:
     * <code>
     * {
     *     "cosBucketName": "youtha",
     *     "cosObjectName": "guide-cdi-intro.iml",
     *     "status": "failure",
     *     "title": "pom.xml"
     * }
     * </code>
     * Response:
     * <code>
     *{
     *     "_id": "6acdb6555c8140e4a451797657e40fb1",
     *     "_rev": "1-682b2af93e628c160fc3345aab112751",
     *     "cosBucketName": "youtha",
     *     "cosObjectName": "guide-cdi-intro.iml",
     *     "status": "failure",
     *     "title": "pom.xml"
     * }
     * </code>
     * @param document The new Document to create.
     * @return The Docuemnt after it has been stored.  This will include a unique ID for the Document.
     */
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Document newDocument(Document document) {
        if(store == null) {
            return null;
        }

        Document newDocument = store.persist(document);
        return newDocument;
    }
}
