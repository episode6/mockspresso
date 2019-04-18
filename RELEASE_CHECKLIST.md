## Mockspresso Release Checklist

**Start Release**

1. Ensure `develop` branch is green on Jenkins and CircleCI
- `git flow release start v<version>`
- Update version in `build.gradle`
- Update version in `CHANGELOG.md`
- Update version in `README.md`
- Update version in `docs/README.md`
- Run `./gradlew gdmcImportSelf` + commit changes to submodule
- Commit changes with message `[VERSION] Release v<version>`
- **DO NOT PUSH**

**Bump Snapshot Version**

1. Checkout `develp` branch
2. Update SNAPSHOT version in `build.gradle`
3. Update SNAPSHOT version in `CHANGELOG.md`
4. Update SNAPSHOT version in `README.md`
5. Update SNAPSHOT version in `docs/README.md`
6. Commit changes with message `[VERSION] Snapshot v<version`
7. **Push both branches**

**Sync Docs**

1. Checkout release branch
2. Run `./gradlew syncDocs`
3. Commit changes with message `[DOCS] Sync v<version>`
4. Push branch

**Release**

1. Wait for green builds on Jenkins and CircleCi
2. `git flow release finish`
3. Resolve merge conflicts on develop
4. Push branches
