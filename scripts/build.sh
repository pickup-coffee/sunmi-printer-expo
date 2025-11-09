#!/bin/bash

# Build script para el paquete npm

echo "🧹 Cleaning previous build..."
rm -rf lib

echo "📦 Building TypeScript..."
tsc

echo "✅ Build completed successfully!"
echo "📁 Generated files:"
ls -la lib/

echo ""
echo "📊 Package size:"
npm pack --dry-run