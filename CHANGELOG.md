# Change Log

## [Unreleased]

### Added

- Limit the output result of found matches.
- Updating the git grep search engine:
    - Set a pattern for files that will be included in the search.
    - Set a pattern for files that will be excluded from the search.
    - Set a limit on the number of matches per file.
    - Set the maximum search depth.

## [0.0.1] 2024-01-18

### Added

- Add a git search engine like [git-grep](https://git-scm.com/docs/git-grep):
    - Set a pattern for matches.
    - Exclude files from search by extension.
    - Include files by extension in your search.
- Basic operations for git repositories:
    - Add local git repository.
    - Delete local git repository.
    - Update local git repository branches.
- Add a git repository provider:
    - Add support for provider types: `GITHUB` and `GITLAB`.
    - Get git repositories from supported providers.
    - Add a git repository provider from supported providers.
    - Remove the git repository provider.
- Get file content from git repository.
