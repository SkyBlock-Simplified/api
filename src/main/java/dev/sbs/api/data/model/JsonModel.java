package dev.sbs.api.data.model;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.json.JsonRepository;

@SuppressWarnings("all")
public interface JsonModel extends Model {

    default <T> T save() {
        return (T) ((JsonRepository) SimplifiedApi.getRepositoryOf(this.getClass())).save(this);
    }

    default <T> T update() {
        return (T) ((JsonRepository) SimplifiedApi.getRepositoryOf(this.getClass())).update(this);
    }

}
