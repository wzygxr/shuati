#!/usr/bin/env bash
set -euo pipefail
cat source_bundle_parts/part_* > v073_text_sources.tar.gz.b64
printf '403638201e3dec164cc7b285c2d817161c1c2ba2c43451e2acaaea293cf22862  v073_text_sources.tar.gz.b64\n' | sha256sum -c -
base64 -d v073_text_sources.tar.gz.b64 > v073_text_sources.tar.gz
printf '29e2776838ff4173ca284601417f0bbfa06bf7f466f31337e8ef1600c1612880  v073_text_sources.tar.gz\n' | sha256sum -c -
mkdir -p v073_text_sources
tar -xzf v073_text_sources.tar.gz -C v073_text_sources
printf 'Recovered into: %s/v073_text_sources\n' "$PWD"
