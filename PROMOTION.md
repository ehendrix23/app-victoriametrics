# Promoting VictoriaMetrics into the permanent Home Assistant branch

This document defines the safe update procedure for `ha-repository`.

## Invariants

The following must not change after Home Assistant installs the app:

- repository URL: `https://github.com/ehendrix23/app-victoriametrics#ha-repository`
- repository branch: `ha-repository`
- app slug: `victoriametrics`
- `repository.yaml` remains at the repository root
- wrapper versions only move forward

These invariants preserve Home Assistant's repository/app identity and keep the
existing app options, persistent `/data` volume, and app configuration directory
associated with the same installation.

## Promoting development code

1. Finish and validate work on a normal feature branch.
2. Confirm the feature branch is based on the intended upstream revision.
3. Replace the `victoriametrics/` contents on `ha-repository` with the validated
   feature version.
4. Preserve `repository.yaml`, this document, and the branch-specific root
   `README.md`.
5. Change `victoriametrics/config.yaml` back from upstream `version: dev` to a
   new wrapper version higher than the currently installed wrapper version.
6. Verify `slug: victoriametrics` is unchanged.
7. Compare the resulting branch against its intended source and review every
   wrapper-only difference.
8. Refresh the Home Assistant App Store and install the offered app update.

## Switching to upstream after merge

When the stream-aggregation work is merged upstream:

1. Select the exact upstream commit/tag to run.
2. Copy the upstream `victoriametrics/` application contents into
   `ha-repository`.
3. Preserve the permanent-branch files listed above.
4. Set a new monotonically increasing wrapper version in
   `victoriametrics/config.yaml`.
5. Confirm `slug: victoriametrics` and all storage/configuration semantics are
   still compatible before promotion.
6. Review the diff so the only intentional differences from upstream are the
   wrapper version and permanent-repository documentation/metadata.
7. Push `ha-repository`; Home Assistant will see an update for the same
   installed app rather than a new app.

Do not remove the existing Home Assistant repository and do not replace its URL
with the official Community Apps URL as part of this promotion. Doing that would
create a different repository/app identity and turn the operation into a data
migration instead of an in-place update.
