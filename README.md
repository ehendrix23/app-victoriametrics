# VictoriaMetrics for Home Assistant — permanent installation branch

This branch is the permanent Home Assistant repository endpoint for Erik's
VictoriaMetrics installation.

## Repository URL

Add this exact URL to Home Assistant and do not change it after installation:

```text
https://github.com/ehendrix23/app-victoriametrics#ha-repository
```

Home Assistant derives the repository identity from the complete repository
URL, including the branch suffix. Keeping this URL unchanged preserves the
installed app identity when the implementation on this branch is updated.

For this URL the repository identifier is:

```text
86fb6935
```

and the installed app identifier is therefore:

```text
86fb6935_victoriametrics
```

The app configuration directory exposed by Home Assistant is:

```text
/addon_configs/86fb6935_victoriametrics
```

## Current source

The app currently contains the stream-aggregation implementation developed on:

```text
ehendrix23/app-victoriametrics
feature/stream-aggregation-config
```

The implementation is based on:

```text
hassio-addons/app-victoriametrics
main @ 90409d8c1699a9e77aaa3c1c5c0a1ae45ddd712d
```

The wrapper version is currently:

```text
0.1.0
```

Wrapper versions are intentionally independent from upstream release versions.
They provide Home Assistant with a monotonically increasing version so changes
to this permanent branch can be installed as normal app updates.

## Future upstream transition

When stream aggregation is merged upstream, this branch should be updated by
replacing the `victoriametrics/` application contents with the desired upstream
release/main contents while preserving:

- this `ha-repository` branch;
- the exact repository URL above;
- `repository.yaml`;
- the app `slug: victoriametrics`;
- the wrapper version progression.

That leaves Home Assistant pointing at the same repository identity and the
same app identity. The application's `/data` volume, Home Assistant options,
and `/addon_configs/86fb6935_victoriametrics` directory therefore remain tied
to the same installed app while its code is updated.

If desired later, the installation can still be migrated to the official
Home Assistant Community Apps repository. That is a separate migration because
the official repository URL has a different Home Assistant repository identity.

## Installation

In Home Assistant:

1. Open **Settings > Apps**.
2. Open the App Store / install-app view.
3. Open the repository management menu.
4. Add exactly:

   ```text
   https://github.com/ehendrix23/app-victoriametrics#ha-repository
   ```

5. Refresh the store if necessary.
6. Open **VictoriaMetrics** from **Erik's VictoriaMetrics**.
7. Install it.

The app can be installed and started without stream aggregation configured.
When stream aggregation is ready to be enabled, place its YAML configuration
in `/addon_configs/86fb6935_victoriametrics` and set the app option
`stream_aggregation_config` to the relative filename.

Example:

```yaml
stream_aggregation_config: stream-aggregation.yaml
```

See `victoriametrics/STREAM_AGGREGATION.md` for the feature behavior and
configuration format. A starter template is available at
`victoriametrics/stream-aggregation.example.yaml`.

## Development rule

Do not develop directly on this branch. Feature development belongs on normal
feature branches. This branch is an installation/promotion branch whose purpose
is to keep the Home Assistant repository pointer stable.
