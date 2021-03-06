package stroom.explorer.impl;

import stroom.docref.DocRef;
import stroom.docref.DocRefInfo;
import stroom.explorer.api.ExplorerActionHandler;
import stroom.explorer.shared.DocumentType;
import stroom.explorer.shared.ExplorerConstants;
import stroom.security.api.SecurityContext;
import stroom.security.shared.DocumentPermissionNames;
import stroom.util.entity.NameValidationUtil;
import stroom.util.shared.PermissionException;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

class FolderExplorerActionHandler implements ExplorerActionHandler {
    private static final String FOLDER = ExplorerConstants.FOLDER;
    private static final String NAME_PATTERN_VALUE = "^[a-zA-Z0-9_\\- \\.\\(\\)]{1,}$";
    private final SecurityContext securityContext;
    private final ExplorerTreeDao explorerTreeDao;

    @Inject
    FolderExplorerActionHandler(final SecurityContext securityContext,
                                final ExplorerTreeDao explorerTreeDao) {
        this.securityContext = securityContext;
        this.explorerTreeDao = explorerTreeDao;
    }

    @Override
    public DocRef createDocument(final String name) {
        NameValidationUtil.validate(NAME_PATTERN_VALUE, name);
        return new DocRef(FOLDER, UUID.randomUUID().toString(), name);
    }

    @Override
    public DocRef copyDocument(final String originalUuid,
                               final String copyUuid,
                               final Map<String, String> otherCopiesByOriginalUuid) {
        final ExplorerTreeNode explorerTreeNode = explorerTreeDao.findByUUID(originalUuid);
        if (explorerTreeNode == null) {
            throw new RuntimeException("Unable to find tree node to copy");
        }

        if (!securityContext.hasDocumentPermission(FOLDER, originalUuid, DocumentPermissionNames.READ)) {
            throw new PermissionException(securityContext.getUserId(), "You do not have permission to read (" + FOLDER + ")");
        }
        return new DocRef(FOLDER, copyUuid, explorerTreeNode.getName());
    }

    @Override
    public DocRef moveDocument(final String uuid) {
        final ExplorerTreeNode explorerTreeNode = explorerTreeDao.findByUUID(uuid);
        if (explorerTreeNode == null) {
            throw new RuntimeException("Unable to find tree node to move");
        }

        if (!securityContext.hasDocumentPermission(FOLDER, uuid, DocumentPermissionNames.READ)) {
            throw new PermissionException(securityContext.getUserId(), "You do not have permission to read (" + FOLDER + ")");
        }
        return explorerTreeNode.getDocRef();
    }

    @Override
    public DocRef renameDocument(final String uuid, final String name) {
        final ExplorerTreeNode explorerTreeNode = explorerTreeDao.findByUUID(uuid);
        if (explorerTreeNode == null) {
            throw new RuntimeException("Unable to find tree node to rename");
        }
        if (!securityContext.hasDocumentPermission(FOLDER, uuid, DocumentPermissionNames.UPDATE)) {
            throw new PermissionException(securityContext.getUserId(), "You do not have permission to update (" + FOLDER + ")");
        }
        NameValidationUtil.validate(NAME_PATTERN_VALUE, name);
        explorerTreeNode.setName(name);
        return explorerTreeNode.getDocRef();
    }

    @Override
    public void deleteDocument(final String uuid) {
        final ExplorerTreeNode explorerTreeNode = explorerTreeDao.findByUUID(uuid);
        if (explorerTreeNode == null) {
            throw new RuntimeException("Unable to find tree node to delete");
        }
        if (!securityContext.hasDocumentPermission(FOLDER, uuid, DocumentPermissionNames.DELETE)) {
            throw new PermissionException(securityContext.getUserId(), "You do not have permission to delete (" + FOLDER + ")");
        }
    }

    @Override
    public DocRefInfo info(final String uuid) {
        final ExplorerTreeNode explorerTreeNode = explorerTreeDao.findByUUID(uuid);
        if (explorerTreeNode == null) {
            throw new RuntimeException("Unable to find tree node to get info");
        }

        if (!securityContext.hasDocumentPermission(FOLDER, uuid, DocumentPermissionNames.READ)) {
            throw new stroom.entity.shared.PermissionException(securityContext.getUserId(), "You do not have permission to read (" + FOLDER + ")");
        }

        return new DocRefInfo.Builder()
                .docRef(new DocRef.Builder()
                        .type(explorerTreeNode.getType())
                        .uuid(explorerTreeNode.getUuid())
                        .name(explorerTreeNode.getName())
                        .build())
                .otherInfo("DB ID: " + explorerTreeNode.getId())
                .build();
    }

    @Override
    public DocumentType getDocumentType() {
        return new DocumentType(1, FolderExplorerActionHandler.FOLDER, FolderExplorerActionHandler.FOLDER);
    }
}
