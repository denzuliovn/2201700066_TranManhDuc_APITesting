package sample

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable as GlobalVariable
import groovy.json.JsonSlurper

public class Common {

    /**
     * Send POST request to create a new user account and return the new user ID
     */
    @Keyword
    def createNewUser(int age, String username, String password, String gender, int expectedStatus) {
        // Find and set variables on the request object
        RequestObject request = findTestObject('Object Repository/POST a new user')
        request.setBodyContent(new com.kms.katalon.core.testobject.impl.HttpTextBodyContent(
            """{
                "age": ${age},
                "avatar": null,
                "gender": "${gender}",
                "password": "${password}",
                "username": "${username}"
            }"""
        ))

        // Send POST request
        ResponseObject response = WS.sendRequest(request)

        // Verify status code
        WS.verifyResponseStatusCode(response, expectedStatus)

        // Extract and return new user ID from response
        def jsonSlurper = new JsonSlurper()
        def jsonResponse = jsonSlurper.parseText(response.getResponseBodyContent())
        int newUserId = jsonResponse.id

        GlobalVariable.globalid = newUserId
        return newUserId
    }

    /**
     * Send GET request to retrieve user information by user ID
     */
    @Keyword
    def findUserById(int id, int expectedStatus) {
        // Find request object and set id variable
        RequestObject request = findTestObject('Object Repository/GET user by id', [('id'): id])

        // Send GET request
        ResponseObject response = WS.sendRequest(request)

        // Verify status code
        WS.verifyResponseStatusCode(response, expectedStatus)

        return response
    }
}