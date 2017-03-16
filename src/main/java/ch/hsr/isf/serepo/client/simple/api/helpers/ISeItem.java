package ch.hsr.isf.serepo.client.simple.api.helpers;

import java.util.List;
import java.util.Map;

import ch.hsr.isf.serepo.client.simple.api.entities.Relation;

public interface ISeItem {

  String getName();

  String getFolder();

  Map<String, Object> getMetadata();

  List<Relation> getRelations();

}
