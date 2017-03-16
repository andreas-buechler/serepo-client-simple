package ch.hsr.isf.serepo.client.simple.commit;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import ch.hsr.isf.serepo.client.simple.Settings;
import ch.hsr.isf.serepo.client.simple.api.entities.CommitMode;
import ch.hsr.isf.serepo.client.simple.api.entities.CreateCommit;
import ch.hsr.isf.serepo.client.simple.api.entities.Relation;
import ch.hsr.isf.serepo.client.simple.api.helpers.ISeItemWithContent;
import ch.hsr.isf.serepo.client.simple.api.helpers.SeItemWithContent;

public class SimpleCommit {

  public SimpleCommit() {}

  public static void main(String[] args) {

    ISeItemWithContent seItemAd1 = createSeItemAd1();
    ISeItemWithContent seItemAd2 = createSeItemAd2();

    // related_decision needs to be defined in the relations.yml file of SE-Repo.
    seItemAd2.getRelations()
             .add(new Relation("related_decision", seItemAd1.getFolder() + "/" + seItemAd1.getName()));

    CreateCommit commit = createCommit("commit 1", CommitMode.ADD_UPDATE_DELETE);

    MultipartFormDataOutput multipart = createMultipart(commit, Arrays.asList(seItemAd1, seItemAd2));

    String commitId = commit("embedded-adl", multipart);
    
    System.out.println("Commit-ID: " + commitId);

  }

  /**
   * Creates a Multipart which will be needed for the SE-Repo API call to commit SE-Items.
   * 
   * @param commit
   * @param seItemsWithContent
   * @return
   */
  private static MultipartFormDataOutput createMultipart(CreateCommit commit, List<ISeItemWithContent> seItemsWithContent) {
    MultipartFormDataOutput multipart = new MultipartFormDataOutput();

    multipart.addFormData("commit", commit, MediaType.APPLICATION_JSON_TYPE);

    int partCounter = 0;
    // We create for each SE-Item a "multipart-pair" with JSON data (metadata_) and "binary" data (content_)
    for (ISeItemWithContent seItem : seItemsWithContent) {
      multipart.addFormData("metadata_" + partCounter, seItem, MediaType.APPLICATION_JSON_TYPE);
      multipart.addFormData("content_" + partCounter, seItem.getContent(), MediaType.valueOf(seItem.getMimeType()), seItem.getName());
      partCounter++;
    }

    return multipart;
  }

  /**
   * Commits the SE-Items (within the Multipart) to the given repository on SE-Repo.
   * 
   * @param toRepository
   * @param multipart
   * @return
   */
  private static String commit(String toRepository, MultipartFormDataOutput multipart) {

    WebTarget api = ClientBuilder.newClient()
                                 .target(Settings.SEREPO_URL)
                                 .path("/repos/" + toRepository + "/commits");

    Response response = api.request(MediaType.TEXT_PLAIN_TYPE)
                           .post(Entity.entity(multipart, MediaType.MULTIPART_FORM_DATA_TYPE));

    String commitId = null;
    if (response.hasEntity()) {
      commitId = response.readEntity(String.class);
    } else {
      commitId = "";
    }
    response.close();
    return commitId;

  }

  /**
   * Creates an SE-Item with possible values of a DecisionMade annotation.
   * 
   * @return
   */
  private static ISeItemWithContent createSeItemAd1() {
    SeItemWithContent seItem = createSeItem("AD01", "decisions");

    // We add some metadata of the annotation.
    Map<String, Object> metadata = seItem.getMetadata();
    metadata.put("solvedProblem", "description of solvedProblem");
    metadata.put("chosenOption", "description of chosenOption");
    metadata.put("rationale", "description of rationale");

    String markdown = "# Title for AD01\n" + "Some description of the Se-Item.";
    seItem.setContent(markdown.getBytes());
    seItem.setMimeType("text/markdown");

    return seItem;
  }

  /**
   * Creates an SE-Item with possible values of a DecisionMade annotation.
   * 
   * @return
   */
  private static ISeItemWithContent createSeItemAd2() {
    SeItemWithContent seItem = createSeItem("AD02", "decisions");

    // We add some metadata of the annotation.
    Map<String, Object> metadata = seItem.getMetadata();
    metadata.put("solvedProblem", "description of solvedProblem");
    metadata.put("chosenOption", "description of chosenOption");
    metadata.put("rationale", "description of rationale");

    String markdown = "# Title for AD02\n" + "Some description of the Se-Item.";
    seItem.setContent(markdown.getBytes());
    seItem.setMimeType("text/markdown");

    return seItem;
  }

  /**
   * Creates a Commit object representing a commit which can be sent to the SE-Repo API.
   * 
   * @param message
   * @param mode
   * @return
   */
  private static CreateCommit createCommit(String message, CommitMode mode) {
    CreateCommit commit = new CreateCommit();
    commit.setMessage(message);
    commit.setMode(mode);
    commit.setUser(Settings.USER);
    return commit;
  }

  /**
   * Creates an extended SE-Item Java Object which can hold content. This object is only used for simple programming purpose.
   * 
   * @param name
   * @param folder
   * @return
   */
  private static SeItemWithContent createSeItem(String name, String folder) {
    SeItemWithContent seItem = new SeItemWithContent();
    seItem.setName(name);
    seItem.setFolder(folder);
    return seItem;
  }

}
