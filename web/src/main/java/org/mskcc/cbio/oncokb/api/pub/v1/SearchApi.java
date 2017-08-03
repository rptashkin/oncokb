package org.mskcc.cbio.oncokb.api.pub.v1;

import io.swagger.annotations.*;
import org.mskcc.cbio.oncokb.model.EvidenceQueries;
import org.mskcc.cbio.oncokb.model.IndicatorQueryResp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-10-19T19:28:21.941Z")

@Api(value = "search", description = "The search API")
public interface SearchApi {

    @ApiOperation(value = "", notes = "General search for possible combinations.", response = IndicatorQueryResp.class, tags = {"Search",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = IndicatorQueryResp.class),
        @ApiResponse(code = 400, message = "Error, error message will be given.", response = IndicatorQueryResp.class)})
//    @RequestMapping(value = "/search",
//        produces = {"application/json"},
//        method = RequestMethod.GET)
    ResponseEntity<IndicatorQueryResp> searchGet(
        @ApiParam(value = "The query ID") @RequestParam(value = "id", required = false) String id
        , @ApiParam(value = "The gene symbol used in Human Genome Organisation.") @RequestParam(value = "hugoSymbol", required = false) String hugoSymbol
        , @ApiParam(value = "The entrez gene ID.") @RequestParam(value = "entrezGeneId", required = false) Integer entrezGeneId
        , @ApiParam(value = "Variant name.") @RequestParam(value = "variant", required = false) String variant
        , @ApiParam(value = "Consequence. Possible value: feature_truncation, frameshift_variant, inframe_deletion, inframe_insertion, start_lost, missense_variant, splice_region_variant, stop_gained, synonymous_variant") @RequestParam(value = "consequence", required = false) String consequence
        , @ApiParam(value = "Protein Start") @RequestParam(value = "proteinStart", required = false) Integer proteinStart
        , @ApiParam(value = "Protein End") @RequestParam(value = "proteinEnd", required = false) Integer proteinEnd
        , @ApiParam(value = "Tumor type name. OncoTree code is supported.") @RequestParam(value = "tumorType", required = false) String tumorType
        , @ApiParam(value = "Tumor type source. OncoTree tumor types are the default setting. We may have customized version, like Quest.", defaultValue = "oncotree") @RequestParam(value = "source", required = false, defaultValue = "oncotree") String source
        , @ApiParam(value = "Level of evidences.") @RequestParam(value = "levels", required = false) String levels
        , @ApiParam(value = "Only show treatments of highest level") @RequestParam(value = "highestLevelOnly", required = false, defaultValue = "FALSE") Boolean highestLevelOnly
        , @ApiParam(value = "Query type. There maybe slight differences between different query types. Currently support web or regular.") @RequestParam(value = "queryType", required = false, defaultValue = "regular") String queryType
        , @ApiParam(value = "HGVS varaint. Its priority is higher than entrezGeneId/hugoSymbol + variant combination") @RequestParam(value = "hgvs", required = false) String hgvs
    );


    @ApiOperation(value = "", notes = "General search for possible combinations.", response = IndicatorQueryResp.class, responseContainer = "List", tags = {"Evidences", "Search",})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = IndicatorQueryResp.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Error, error message will be given.", response = IndicatorQueryResp.class)})
//    @RequestMapping(value = "/search",
//        consumes = {"application/json"},
//        produces = {"application/json"},
//        method = RequestMethod.POST)
    ResponseEntity<List<IndicatorQueryResp>> searchPost(@ApiParam(value = "List of queries. Please see swagger.json for request body format.", required = true) @RequestBody(required = true) EvidenceQueries body
    );
}
