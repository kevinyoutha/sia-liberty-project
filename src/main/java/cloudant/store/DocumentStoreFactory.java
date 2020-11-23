package cloudant.store;

public class DocumentStoreFactory {

    private static DocumentStore instance;
    static {
        CloudantDocumentStore cvif = new CloudantDocumentStore();
        if (cvif.getDB() != null) {
            instance = cvif;
        }
    }

    public static DocumentStore getInstance() {
        return instance;
    }
}
