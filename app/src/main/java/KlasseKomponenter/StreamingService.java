package KlasseKomponenter;

public class StreamingService {
    private String providerName;
    private int providerId;

    public StreamingService(String providerName, int providerId) {
        this.providerName = providerName;
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String toString(){
        return providerName +" "+providerId;
    }
}
