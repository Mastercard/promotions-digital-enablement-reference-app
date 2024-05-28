package com.mastercard.developer.controller;

import com.mastercard.developer.exception.InvalidRequest;
import com.mastercard.developer.service.AccountService;
import com.mastercard.developer.validator.AccountValidator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.AccountSearch;
import org.openapitools.client.model.PagedAccountSearchItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = "/accounts", produces = {"application/json"})
public class AccountController {

    private final AccountValidator accountValidator;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountValidator accountValidator, AccountService accountService) {
        this.accountService = accountService;
        this.accountValidator = accountValidator;
    }

    /**
     * @param offset The number of items to offset the start of the list from (Pagination)
     * @param limit Can be used to limit the amount of results returned from a query (Pagination)
     * @param accountSearch Account search request
     * @return Paged Account search response
     */
    @PostMapping(value = "/searches", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @ApiOperation(value = "Search Account", nickname = "searchAccount")
    @ApiImplicitParams({@ApiImplicitParam(
            name = "offset",
            dataType = "int",
            paramType = "query",
            example = "0",
            examples = @Example(@ExampleProperty("0")),
            value = "The number of items to offset the start of the list from (Pagination)",
            dataTypeClass = Integer.class
    ), @ApiImplicitParam(
            name = "limit",
            dataType = "int",
            paramType = "query",
            example = "25",
            examples = @Example(@ExampleProperty("25")),
            value = "Can be used to limit the amount of results returned from a query (Pagination)",
            dataTypeClass = Integer.class
    )})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Retrieved Account")
    })
    public PagedAccountSearchItem searchAccount(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                      @RequestParam(value = "limit", required = false, defaultValue = "25") int limit,
                                                      @RequestBody AccountSearch accountSearch) {
        accountValidator.validateSearchAccount(accountSearch);
        try {
            PagedAccountSearchItem response = accountService.searchAccount(offset, limit, accountSearch);
            if (response != null && response.getItems() != null) {
                log.debug("Method : searchAccount, Message :Successfully got the accounts" + response);
                return response;
            }
        } catch (ApiException ex) {
            log.error("Method : searchAccount, Message : Failed to search the Account");
            throw new InvalidRequest(ex.getResponseBody());
        }
        throw new InvalidRequest(HttpStatus.BAD_REQUEST);
    }
}
