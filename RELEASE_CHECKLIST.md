## Mockspresso Release Checklist

**Start Release**

1. Ensure `develop` branch is green on Jenkins and CircleCI
2. `git flow release start v<version>`
3. Update version in `build.gradle`
4. Update version in `docs/CHANGELOG.md`
5. Update version in `docs/README.md`
6. Run `./gradlew gdmcImportSelf` + commit changes to submodule
7. Commit changes with message `[VERSION] Release v<version>`
8. **DO NOT PUSH**

**Bump Snapshot Version**

1. Checkout `develp` branch
2. Update SNAPSHOT version in `build.gradle`
3. Update SNAPSHOT version in `docs/CHANGELOG.md`
4. Update SNAPSHOT version in `docs/README.md`
5. Commit changes with message `[VERSION] Snapshot v<version>`
6. **Push both branches**

**Sync Docs**

1. Checkout release branch
2. Run `./gradlew syncDocs`
3. Commit changes with message `[DOCS] Sync v<version>`
4. Push branch

**Release**

1. Wait for green builds on Jenkins and CircleCi
2. `git flow release finish` (tag with `v<version>`)
3. Resolve merge conflicts on develop
4. Push branches
