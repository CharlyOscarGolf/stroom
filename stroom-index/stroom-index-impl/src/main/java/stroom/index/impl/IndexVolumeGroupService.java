package stroom.index.impl;

import stroom.index.shared.IndexVolumeGroup;

import java.util.List;

public interface IndexVolumeGroupService {
    List<String> getNames();

    List<IndexVolumeGroup> getAll();

    IndexVolumeGroup create();

    IndexVolumeGroup getOrCreate(String name);

    IndexVolumeGroup update(IndexVolumeGroup indexVolumeGroup);

    IndexVolumeGroup get(int id);

    void delete(int id);
}
