# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.2.0] - 2026-11-06

### Added
- Client identifies itself with name and version; optional app name argument.

### Changed
- Package author and license holder are now kwtSMS.
- Bulk SMS example reads recipients from a file.

## [0.1.4] - 2026-03-14

### Added
- Integration tests in CI on release tags.
- Extra fields on bulk send results.
- README additions.

### Fixed
- Bug fixes.

## [0.1.3] - 2026-03-14

### Added
- Country-specific phone validation.
- Trunk prefix handling in phone normalization.
- Secret scanning workflow.

## [0.1.2] - 2026-03-06

### Added
- Ktor endpoint example.
- Production OTP example with database adapters, CAPTCHA verifiers and device attestation.

### Changed
- Examples renumbered.

## [0.1.1] - 2026-03-06

### Added
- README badges, Best Practices section and examples table.
- Android Google Play Integrity API guidance.

## [0.1.0] - 2026-03-06

Initial release of the `kwtsms` Kotlin client library.

### Added
- `KwtSMS` client with `verify()`, `balance()`, `send()`, `sendBulk()`, `validate()`, `senderIds()`, `coverage()`, `status()` and `deliveryReport()`
- Phone normalization and message cleaning
- Error code map with developer-friendly messages
- Zero dependencies
- JSONL logging
- Test suite
- README, examples, CONTRIBUTING and LICENSE

[Unreleased]: https://github.com/kwtsms/kwtsms-kotlin/compare/v0.2.0...HEAD
[0.2.0]: https://github.com/kwtsms/kwtsms-kotlin/compare/v0.1.4...v0.2.0
[0.1.4]: https://github.com/kwtsms/kwtsms-kotlin/compare/v0.1.3...v0.1.4
[0.1.3]: https://github.com/kwtsms/kwtsms-kotlin/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/kwtsms/kwtsms-kotlin/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/kwtsms/kwtsms-kotlin/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/kwtsms/kwtsms-kotlin/releases/tag/v0.1.0
