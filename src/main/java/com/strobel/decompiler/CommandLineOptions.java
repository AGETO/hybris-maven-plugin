package com.strobel.decompiler;

import java.util.ArrayList;
import java.util.List;

public class CommandLineOptions {
    private final List<String> _inputs = new ArrayList<>();
    private boolean            _printUsage;
    private boolean            _mergeVariables;
    private boolean            _forceExplicitImports;
    private boolean            _collapseImports;
    private boolean            _forceExplicitTypeArguments;
    private boolean            _retainRedundantCasts;
    private boolean            _flattenSwitchBlocks;
    private boolean            _showSyntheticMembers;
    private boolean            _bytecodeAst;
    private boolean            _rawBytecode;
    private boolean            _unoptimized;
    private boolean            _excludeNestedTypes;
    private String             _outputDirectory;
    private String             _jarFile;
    private boolean            _includeLineNumbers;
    private boolean            _stretchLines;
    private boolean            _showDebugLineNumbers;
    private boolean            _retainPointlessSwitches;
    private int                _verboseLevel;
    private boolean            _useLightColorScheme;
    private boolean            _isUnicodeOutputEnabled;
    private boolean            _isEagerMethodLoadingEnabled;
    private boolean            _simplifyMemberReferences;
    private boolean            _disableForEachTransforms;
    private boolean            _printVersion;
    private boolean            _suppressBanner;

    public CommandLineOptions() {
    }

    public final List<String> getInputs() {
        return this._inputs;
    }

    public final boolean isBytecodeAst() {
        return this._bytecodeAst;
    }

    public final void setBytecodeAst(boolean bytecodeAst) {
        this._bytecodeAst = bytecodeAst;
    }

    public final boolean isRawBytecode() {
        return this._rawBytecode;
    }

    public final void setRawBytecode(boolean rawBytecode) {
        this._rawBytecode = rawBytecode;
    }

    public final boolean getFlattenSwitchBlocks() {
        return this._flattenSwitchBlocks;
    }

    public final void setFlattenSwitchBlocks(boolean flattenSwitchBlocks) {
        this._flattenSwitchBlocks = flattenSwitchBlocks;
    }

    public final boolean getExcludeNestedTypes() {
        return this._excludeNestedTypes;
    }

    public final void setExcludeNestedTypes(boolean excludeNestedTypes) {
        this._excludeNestedTypes = excludeNestedTypes;
    }

    public final boolean getCollapseImports() {
        return this._collapseImports;
    }

    public final void setCollapseImports(boolean collapseImports) {
        this._collapseImports = collapseImports;
    }

    public final boolean getForceExplicitTypeArguments() {
        return this._forceExplicitTypeArguments;
    }

    public final void setForceExplicitTypeArguments(boolean forceExplicitTypeArguments) {
        this._forceExplicitTypeArguments = forceExplicitTypeArguments;
    }

    public boolean getRetainRedundantCasts() {
        return this._retainRedundantCasts;
    }

    public void setRetainRedundantCasts(boolean retainRedundantCasts) {
        this._retainRedundantCasts = retainRedundantCasts;
    }

    public final boolean isUnoptimized() {
        return this._unoptimized;
    }

    public final void setUnoptimized(boolean unoptimized) {
        this._unoptimized = unoptimized;
    }

    public final boolean getShowSyntheticMembers() {
        return this._showSyntheticMembers;
    }

    public final void setShowSyntheticMembers(boolean showSyntheticMembers) {
        this._showSyntheticMembers = showSyntheticMembers;
    }

    public final boolean getPrintUsage() {
        return this._printUsage;
    }

    public final void setPrintUsage(boolean printUsage) {
        this._printUsage = printUsage;
    }

    public final String getOutputDirectory() {
        return this._outputDirectory;
    }

    public final void setOutputDirectory(String outputDirectory) {
        this._outputDirectory = outputDirectory;
    }

    public final String getJarFile() {
        return this._jarFile;
    }

    public final void setJarFile(String jarFile) {
        this._jarFile = jarFile;
    }

    public final boolean getIncludeLineNumbers() {
        return this._includeLineNumbers;
    }

    public final void setIncludeLineNumbers(boolean includeLineNumbers) {
        this._includeLineNumbers = includeLineNumbers;
    }

    public final boolean getStretchLines() {
        return this._stretchLines;
    }

    public final void setStretchLines(boolean stretchLines) {
        this._stretchLines = stretchLines;
    }

    public final boolean getShowDebugLineNumbers() {
        return this._showDebugLineNumbers;
    }

    public final void setShowDebugLineNumbers(boolean showDebugLineNumbers) {
        this._showDebugLineNumbers = showDebugLineNumbers;
    }

    public final boolean getRetainPointlessSwitches() {
        return this._retainPointlessSwitches;
    }

    public final void setRetainPointlessSwitches(boolean retainPointlessSwitches) {
        this._retainPointlessSwitches = retainPointlessSwitches;
    }

    public final int getVerboseLevel() {
        return this._verboseLevel;
    }

    public final void setVerboseLevel(int verboseLevel) {
        this._verboseLevel = verboseLevel;
    }

    public final boolean getUseLightColorScheme() {
        return this._useLightColorScheme;
    }

    public final void setUseLightColorScheme(boolean useLightColorScheme) {
        this._useLightColorScheme = useLightColorScheme;
    }

    public final boolean isUnicodeOutputEnabled() {
        return this._isUnicodeOutputEnabled;
    }

    public final void setUnicodeOutputEnabled(boolean unicodeOutputEnabled) {
        this._isUnicodeOutputEnabled = unicodeOutputEnabled;
    }

    public final boolean getMergeVariables() {
        return this._mergeVariables;
    }

    public final void setMergeVariables(boolean mergeVariables) {
        this._mergeVariables = mergeVariables;
    }

    public final boolean isEagerMethodLoadingEnabled() {
        return this._isEagerMethodLoadingEnabled;
    }

    public final void setEagerMethodLoadingEnabled(boolean isEagerMethodLoadingEnabled) {
        this._isEagerMethodLoadingEnabled = isEagerMethodLoadingEnabled;
    }

    public final boolean getSimplifyMemberReferences() {
        return this._simplifyMemberReferences;
    }

    public final void setSimplifyMemberReferences(boolean simplifyMemberReferences) {
        this._simplifyMemberReferences = simplifyMemberReferences;
    }

    public final boolean getDisableForEachTransforms() {
        return this._disableForEachTransforms;
    }

    public final void setDisableForEachTransforms(boolean disableForEachTransforms) {
        this._disableForEachTransforms = disableForEachTransforms;
    }

    public final boolean getPrintVersion() {
        return this._printVersion;
    }

    public final void setPrintVersion(boolean printVersion) {
        this._printVersion = printVersion;
    }

    public final boolean getSuppressBanner() {
        return this._suppressBanner;
    }

    public final void setSuppressBanner(boolean suppressBanner) {
        this._suppressBanner = suppressBanner;
    }
}
