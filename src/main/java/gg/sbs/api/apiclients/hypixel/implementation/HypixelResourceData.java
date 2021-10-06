package gg.sbs.api.apiclients.hypixel.implementation;

import feign.RequestLine;
import gg.sbs.api.apiclients.hypixel.response.resource.ResourceCollectionsResponse;
import gg.sbs.api.apiclients.hypixel.response.resource.ResourceItemsResponse;
import gg.sbs.api.apiclients.hypixel.response.resource.ResourceSkillsResponse;

public interface HypixelResourceData extends HypixelDataInterface {

    @RequestLine("GET /resources/skyblock/skills")
    ResourceSkillsResponse getSkills();

    @RequestLine("GET /resources/skyblock/collections")
    ResourceCollectionsResponse getCollections();

    @RequestLine("GET /resources/skyblock/items")
    ResourceItemsResponse getItems();

}