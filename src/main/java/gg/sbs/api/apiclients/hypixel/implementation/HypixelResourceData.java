package gg.sbs.api.apiclients.hypixel.implementation;

import feign.RequestLine;
import gg.sbs.api.apiclients.hypixel.response.ResourceCollectionsResponse;
import gg.sbs.api.apiclients.hypixel.response.ResourceItemsResponse;
import gg.sbs.api.apiclients.hypixel.response.ResourceSkillsResponse;

public interface HypixelResourceData extends HypixelDataInterface {
    @RequestLine("GET /resources/skyblock/skills")
    ResourceSkillsResponse getSkills();

    @RequestLine("GET /resources/skyblock/collections")
    ResourceCollectionsResponse getCollections();

    @RequestLine("GET /resources/skyblock/items")
    ResourceItemsResponse getItems();
}