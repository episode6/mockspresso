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
- Update SNAPSHOT version in `build.gradle`
- Update SNAPSHOT version in `CHANGELOG.md`
- Update SNAPSHOT version in `README.md`
- Update SNAPSHOT version in `docs/README.md`
- Commit changes with message `[VERSION] Snapshot v<version`
- **Push both branches**

**Sync Docs**

1. Checkout release branch
- Run `./gradlew syncDocs`
- Commit changes with message `[DOCS] Sync v<version>`
- Push branch

**Release**

1. Wait for green builds on Jenkins and CircleCi
- `git flow release finish`
- Resolve merge conflicts on develop
- Push branches
