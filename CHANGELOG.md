# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.69.1] - 2025-01-08

### Added
- **Guard functionality for REST mock responses**: New `isGuard` field allows mock responses to act as header interceptors
  - Guards evaluate first, independently of Response Strategies
  - If header validation fails, guards return their configured response immediately (e.g., 401 for invalid JWT)
  - If header validation passes, processing continues to other mock responses
  - Perfect for JWT token validation, API key authentication, and other header-based security checks
- **UI enhancements**: Added tooltip to "Is Guard" checkbox explaining functionality
- **Comprehensive integration**: Guard functionality works across all response strategies including Multiple Response Strategy

### Changed
- Guards are evaluated independently of Response Strategy settings (JSON Path, Header Query Match, etc.)
- Guard responses are filtered out from normal response selection logic

### Technical Details
- Added `isGuard` field to `RestMockResponse` domain model
- Updated services: `CreateRestMockResponseService` and `UpdateRestMockResponseService` for proper persistence
- Modified `AbstractRestServiceController` to evaluate guards before any response strategy
- Enhanced React frontend with informative tooltip

---

## [1.69.0] - 2024-12-XX

### Added
- Multiple response strategies feature
- XML import/export improvements

### Fixed
- Various bug fixes and stability improvements

---

*This changelog was started with version 1.69.1. Previous versions may not be documented here.*