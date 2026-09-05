# Stream aggregation

VictoriaMetrics can aggregate selected incoming time series before they are
written to storage. This is useful when high-frequency raw samples are useful
for short-term systems but only summaries are needed in VictoriaMetrics for
long-term history.

Stream aggregation applies to all data ingested by VictoriaMetrics, whether it
is scraped by this app or pushed through one of VictoriaMetrics' ingestion
protocols.

## Enable stream aggregation

Create a YAML file in this app's configuration directory. For example:

```text
stream-aggregation.yaml
```

Then set the app option to that relative path:

```yaml
stream_aggregation_config: stream-aggregation.yaml
```

Nested relative paths are supported. Absolute paths and paths containing a
parent (`..`) segment are rejected so the option remains confined to the app's
read-only configuration directory.

The app validates the file with VictoriaMetrics before starting the server. An
invalid or missing configuration therefore stops startup instead of silently
running without the expected aggregation policy.

## Example

A generic gauge rule can retain 15-minute summaries instead of every matching
raw sample:

```yaml
- name: example-gauges-15m
  match: '{__name__=~"example_temperature|example_power"}'
  interval: 15m
  outputs:
    - avg
    - min
    - max
    - last
    - count_samples
    - "quantiles(0.5, 0.95)"
```

A cumulative counter that may already be non-zero when VictoriaMetrics starts
should normally use `increase_prometheus` plus `last`:

```yaml
- name: example-counters-15m
  match: '{__name__=~"example_energy_total|example_cost_total"}'
  interval: 15m
  outputs:
    - increase_prometheus
    - last
```

VictoriaMetrics' plain `increase` output assumes a newly seen counter starts at
zero. If the first sample seen after VictoriaMetrics starts is, for example,
`12345`, that baseline can be counted as an increase. `increase_prometheus`
ignores the first observed sample for each newly seen series, which is generally
safer for Home Assistant cumulative energy and cost totals. The trade-off is
that the first delta after aggregation state is lost or reset cannot be
reconstructed from that output alone, which is one reason retaining `last` is
useful as well.

The file can contain multiple independent rules, including multiple intervals
for the same input series. For example, gauges can have full 15-minute summary
outputs and a second hourly rule containing only exact hourly quantiles over the
original incoming samples.

## Raw input behavior

When `-streamAggr.config` is enabled, VictoriaMetrics stores aggregated output
and also stores raw samples that did not match any aggregation rule. Raw input
samples that matched a rule are dropped by default.

This behavior is important: enabling a broad rule can intentionally replace
raw storage for every matching series. Test selectors carefully before using
them against important data.

VictoriaMetrics itself provides `-streamAggr.keepInput` and
`-streamAggr.dropInput` flags for changing those defaults. This app does not
currently expose either flag as an option; the initial implementation keeps
the upstream default behavior deliberately small and predictable.

## What stream aggregation is not

Stream aggregation happens at ingestion time. It does not revisit old data and
it does not progressively reduce the resolution of already stored history as
that history ages.

For example, a 15-minute rule means matching incoming raw samples are combined
into 15-minute outputs before storage. Samples that were already stored before
the rule was enabled remain unchanged.

## Upstream documentation

See the VictoriaMetrics stream aggregation documentation for the complete rule
format, matching syntax, aggregation outputs, label handling, reload behavior,
and operational details:

<https://docs.victoriametrics.com/victoriametrics/stream-aggregation/>
