package tastr.tastr;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


class YelpAPI implements Runnable{

     private static final String API_HOST = "api.yelp.com";
     private static final String DEFAULT_TERM = "dinner";
     private static final String DEFAULT_LOCATION = "San Francisco, CA";
     private static final int SEARCH_LIMIT = 3;
     private static final String SEARCH_PATH = "/v2/search";
     private static final String BUSINESS_PATH = "/v2/business";

    /*
     * Update OAuth credentials below from the YelpAPI Developers API site:
     * http://www.yelp.com/developers/getting_started/api_access
     */
     private static final String CONSUMER_KEY = "cXrLIyuf1dhXyCeoKCRkHA";
     private static final String CONSUMER_SECRET = "nvdY0PsLigihJ6UoJb6HKycwvvE";
     private static final String TOKEN = "N8fy1BW4z_dyqruZXtrA709udr4pmJ6U";
     private static final String TOKEN_SECRET = "DrMxnxYFGYPSb7OD179A5FoGYnU";

    private OAuthService service;
    private Token accessToken;

    private static String YelpInfo = "";

    public String getYelpInfo(){
        return YelpInfo;
    }

   static private void setYelpInfo(String info){

        YelpInfo = info;
    }

    /**
     * Setup the YelpAPI API OAuth credentials.
     */
    public YelpAPI() {

        this.service = new ServiceBuilder().provider(TwoStepOAuth.class).apiKey(YelpAPI.CONSUMER_KEY).apiSecret(YelpAPI.CONSUMER_SECRET)
                .build();
        this.accessToken = new Token(YelpAPI.TOKEN, YelpAPI.TOKEN_SECRET);
    }

    /**
     * Creates and sends a request to the Search API by term and location.
     * <p>
     * See
     * <a href="http://www.yelp.com/developers/documentation/v2/search_api">YelpAPI
     * Search API V2</a> for more info.
     *
     * @param term
     *            <tt>String</tt> of the search term to be queried
     * @param location
     *            <tt>String</tt> of the location
     * @return <tt>String</tt> JSON Response
     */
    private String searchForBusinessesByLocation(String term, String location) {

        OAuthRequest request = createOAuthRequest(SEARCH_PATH);
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        request.addQuerystringParameter("limit", String.valueOf(SEARCH_LIMIT));
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and sends a request to the Business API by business ID.
     * <p>
     * See
     * <a href="http://www.yelp.com/developers/documentation/v2/business">YelpAPI
     * Business API V2</a> for more info.
     *
     * @param businessID
     *            <tt>String</tt> business ID of the requested business
     * @return <tt>String</tt> JSON Response
     */
    private String searchByBusinessId(String businessID) {

        OAuthRequest request = createOAuthRequest(BUSINESS_PATH + "/" + businessID);
        return sendRequestAndGetResponse(request);
    }

    /**
     * Creates and returns an {@link OAuthRequest} based on the API endpoint
     * specified.
     *
     * @param path
     *            API endpoint to be queried
     * @return <tt>OAuthRequest</tt>
     */
    private OAuthRequest createOAuthRequest(String path) {

        OAuthRequest request = new OAuthRequest(Verb.GET, "https://" + API_HOST + path);
        return request;
    }

    /**
     * Sends an {@link OAuthRequest} and returns the {@link Response} body.
     *
     * @param request
     *            {@link OAuthRequest} corresponding to the API request
     * @return <tt>String</tt> body of API response
     */
    private String sendRequestAndGetResponse(OAuthRequest request) {
        System.out.println("Querying " + request.getCompleteUrl() + " ...");
        this.service.signRequest(this.accessToken, request);

        Response response = request.send();
        return response.getBody();
    }

    /**
     * Queries the Search API based on the command line arguments and takes the
     * first result to query the Business API.
     *
     * @param yelpApi
     *            <tt>YelpAPI</tt> service instance
     * @param yelpApiCli
     *            <tt>YelpAPICLI</tt> command line arguments
     */
     private static void queryAPI(YelpAPI yelpApi, YelpAPICLI yelpApiCli) {

        String searchResponseJSON = yelpApi.searchForBusinessesByLocation(yelpApiCli.term, yelpApiCli.location);

        JSONParser parser = new JSONParser();
        JSONObject response = null;
        try {
            response = (JSONObject) parser.parse(searchResponseJSON);
        } catch (ParseException pe) {
            System.out.println("Error: could not parse JSON response:");
            System.out.println(searchResponseJSON);
            System.exit(1);
        }

        JSONArray businesses = (JSONArray) response.get("businesses");
        JSONObject firstBusiness = (JSONObject) businesses.get(0);
        String firstBusinessID = firstBusiness.get("id").toString();
        System.out.println(String.format("%s businesses found, querying business info for the top result \"%s\" ...",
                businesses.size(), firstBusinessID));

        // Select the first business and display business details
        String businessResponseJSON = yelpApi.searchByBusinessId(firstBusinessID);
        System.out.println(String.format("Result for business \"%s\" found:", firstBusinessID));
        System.out.println(businessResponseJSON);
        setYelpInfo(businessResponseJSON);


    }

    /**
     * Command-line interface for the sample YelpAPI API runner.
     */
   static class YelpAPICLI {
        @Parameter(names = { "-q", "--term" }, description = "Search Query Term")
        public final String term = DEFAULT_TERM;

        @Parameter(names = { "-l", "--location" }, description = "Location to be Queried")
        public final String location = DEFAULT_LOCATION;
    }

    public void run(){


        YelpAPICLI yelpApiCli = new YelpAPICLI();
        new JCommander(yelpApiCli);

        YelpAPI yelpApi = new YelpAPI();
        queryAPI(yelpApi, yelpApiCli);}

    }





