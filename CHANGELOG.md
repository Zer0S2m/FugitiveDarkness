# Change Log

## [Unreleased]

### Added

- The first prototype of the system is the file structure of the project:
    - Viewing the file structure of the project.
    - Viewing information about commits.
    - View the number of todos in the project.
- Improving the search engine:
    - Set the search areas - file or directory.
- Scheduled tasks:
    - Functionality for creating scheduled tasks.
    - Rules for running scheduled tasks.
- Functionality for updating the repository in the form of scheduled tasks.

### Security

- The postgresql package has been updated - CVE-2024-1597

### Fixed

- When collecting search statistics in the search engine, sometimes it occurs that the speed of search execution 
  is almost zero and an arithmetic error occurs (cannot be divided by zero)
- Closing the client for the database.

## [0.0.7] 2024-04-10

### Added

- Update search statistics - average time to process a file.
- Filter for the file .gitignore for use in the search engine for local repositories.
- The link to the file when viewing it is a URL request.
- Output error information when receiving repositories.

### Fixed

- Remove the full path to the file from the file link.
- Specify the current repository branch in the search engine without JGIT.

## [0.0.6] 2024-03-30

### Added

- A new search engine for searching through docx files.
- Showing an error when setting environment variables.
- Indexes of the found items in the line.
- Statistics output in the search engine for git projects.
- Unpacking a git project.

### Fixed:

- Fix an error when retrieving the entire file - an unterminated string.
- Local repositories search in a chaotic state.
- Implement a regular expression pattern when highlighting matches (client).

## [0.0.5] 2024-02-25

### Added

- Application build and launch system.
- Added a system of local repositories in the file system:
    - Addition.
    - Search.
    - Delete (rows in the database).

### Removed

- Removed the group parameter when adding a repository.

### Fixed:

- No add button if no repositories exist.

## [0.0.4] 2024-02-02

### Added

- Basic exceptions for git providers.
- Search engine:
    - Clone repositories from a remote host (From git providers).
    - Use search splitting into virtual threads.

### Fixed

- Fix pack empty repository for search.
- Fix search exception - MissingObjectException.

## [0.0.3] 2024-01-28

### Added

- Add logging of incoming requests and responses.
- Notes interaction system for search matches:
    - Retrieving all notes for matches.
    - Deleting a note for a match.
    - Create a note for matches.
    - Editing a note for matches.
- System of interaction with search filters:
    - Removing a search filter.
    - Saving a search filter.
    - Getting a search filters.

## [0.0.2] 2024-01-24

### Added

- Limit the output result of found matches.
- Updating the git grep search engine:
    - Set a pattern for files that will be included in the search.
    - Set a pattern for files that will be excluded from the search.
    - Set a limit on the number of matches per file (_[more about](https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---max-countltnumgt)_).
    - Set the maximum search depth (_[more about](https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---max-depthltdepthgt)_).
    - Set the code preview BEFORE and AFTER the match (_[more about](https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---contextltnumgt)_).
    - Set the code preview BEFORE a match (_[more about](https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---after-contextltnumgt)_).
    - Set the code preview AFTER match (_[more about](https://git-scm.com/docs/git-grep#Documentation/git-grep.txt---before-contextltnumgt)_).

### Fixed

- Closing the client for the database.
- Fixed double commit .git.

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
