/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bunjlabs.fuga.foundation;

import com.bunjlabs.fuga.foundation.http.Status;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

/**
 * Helper class that contains useful methods to generate responses.
 */
public class Results implements Status {

    /**
     *
     * @return response with ok (200) status.
     */
    public static Result ok() {
        return new Result().status(OK);
    }

    /**
     *
     * @param is InputStream content.
     * @return response with ok (200) status and input stream content.
     */
    public static Result ok(InputStream is) {
        return new Result(is).status(OK);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with ok (200) status and bytes content.
     */
    public static Result ok(byte[] bytes) {
        return new Result(bytes).status(OK);
    }

    /**
     *
     * @param s String content
     * @return response with ok (200) status and string content.
     */
    public static Result ok(String s) {
        return new Result(s).status(OK);
    }

    /**
     *
     * @param f File content.
     * @return response with ok (200) status and file content.
     * @throws IOException if file read error
     */
    public static Result ok(File f) throws IOException {
        return new Result(f).status(OK);
    }

    /**
     *
     * @param json Json content.
     * @return response with ok (200) status and json content.
     */
    public static Result ok(JSONObject json) {
        return new Result(json.toString()).status(OK).asJson();
    }

    /**
     *
     * @return response with bad request (400) status.
     */
    public static Result badRequest() {
        return new Result().status(BAD_REQUEST);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with bad request (400) status and input stream content.
     */
    public static Result badRequest(InputStream is) {
        return new Result(is).status(BAD_REQUEST);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with bad request (400) status and bytes content.
     */
    public static Result badRequest(byte[] bytes) {
        return new Result(bytes).status(BAD_REQUEST);
    }

    /**
     *
     * @param s String content.
     * @return response with bad request (400) status and string content.
     */
    public static Result badRequest(String s) {
        return new Result(s).status(BAD_REQUEST);
    }

    /**
     *
     * @param f File content.
     * @return response with bad request (400) status and file content.
     * @throws IOException if file read error
     */
    public static Result badRequest(File f) throws IOException {
        return new Result(f).status(BAD_REQUEST);
    }

    /**
     *
     * @param json Json content.
     * @return response with bad request (400) status and json content.
     */
    public static Result badRequest(JSONObject json) {
        return new Result(json.toString()).status(BAD_REQUEST).asJson();
    }

    /**
     *
     * @return response with created (201) status.
     */
    public static Result created() {
        return new Result().status(CREATED);
    }

    /**
     *
     * @param is InputStream content.
     * @return response with created (201) status and input stream content.
     */
    public static Result created(InputStream is) {
        return new Result(is).status(CREATED);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with created (201) status and bytes content.
     */
    public static Result created(byte[] bytes) {
        return new Result(bytes).status(CREATED);
    }

    /**
     *
     * @param s String content.
     * @return response with created (201) status and string content.
     */
    public static Result created(String s) {
        return new Result(s).status(CREATED);
    }

    /**
     *
     * @param f File content.
     * @return response with created (201) status and file content.
     * @throws IOException if file read error
     */
    public static Result created(File f) throws IOException {
        return new Result(f).status(CREATED);
    }

    /**
     *
     * @param json Json content.
     * @return response with created (201) status and json content.
     */
    public static Result created(JSONObject json) {
        return new Result(json.toString()).status(CREATED).asJson();
    }

    /**
     *
     * @return response with forbidden (403) status.
     */
    public static Result forbidden() {
        return new Result().status(FORBIDDEN);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with forbidden (403) status and input stream content.
     */
    public static Result forbidden(InputStream is) {
        return new Result(is).status(FORBIDDEN);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with forbidden (403) status and bytes content.
     */
    public static Result forbidden(byte[] bytes) {
        return new Result(bytes).status(FORBIDDEN);
    }

    /**
     *
     * @param s String content.
     * @return response with forbidden (403) status and string content.
     */
    public static Result forbidden(String s) {
        return new Result(s).status(FORBIDDEN);
    }

    /**
     *
     * @param f File content.
     * @return response with forbidden (403) status and file content.
     * @throws IOException if file read error
     */
    public static Result forbidden(File f) throws IOException {
        return new Result(f).status(FORBIDDEN);
    }

    /**
     *
     * @param json Json content.
     * @return response with forbidden (403) status and json content.
     */
    public static Result forbidden(JSONObject json) {
        return new Result(json.toString()).status(FORBIDDEN);
    }

    /**
     *
     * @return response with no content (204) status.
     */
    public static Result nothing() {
        return noContent();
    }

    /**
     *
     * @return response with no content (204) status.
     */
    public static Result noContent() {
        return new Result().status(NO_CONTENT);
    }

    /**
     *
     * @param url Location url.
     * @return response with found (302) status and location url.
     */
    public static Result found(String url) {
        return new Result().status(FOUND).header("Location", url);
    }

    /**
     *
     * @param url Location url.
     * @return response with moved permanently (301) status and location url.
     */
    public static Result movedPermanently(String url) {
        return new Result().status(MOVED_PERMANENTLY).header("Location", url);
    }

    /**
     *
     * @param url Location url.
     * @return response with see other (303) status and location url.
     */
    public static Result redirect(String url) {
        return seeOther(url);
    }

    /**
     *
     * @param url Location url.
     * @return response with see other (303) status and location url.
     */
    public static Result seeOther(String url) {
        return new Result().status(SEE_OTHER).header("Location", url);
    }

    /**
     *
     * @param url Location url.
     * @return response with temporary redirect (307) status and location url.
     */
    public static Result temporaryRedirect(String url) {
        return new Result().status(TEMPORARY_REDIRECT).header("Location", url);
    }

    /**
     *
     * @return response with internal server error (500) status.
     */
    public static Result internalServerError() {
        return new Result().status(INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with internal server error (500) status and input stream
     * content.
     */
    public static Result internalServerError(InputStream is) {
        return new Result(is).status(INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with internal server error (500) status and bytes
     * content.
     */
    public static Result internalServerError(byte[] bytes) {
        return new Result(bytes).status(INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param s String content.
     * @return response with internal server error (500) status and string
     * content.
     */
    public static Result internalServerError(String s) {
        return new Result(s).status(INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param f File content.
     * @return response with internal server error (500) status and file
     * content.
     * @throws IOException if file read error
     */
    public static Result internalServerError(File f) throws IOException {
        return new Result(f).status(INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param json Json content.
     * @return response with internal server error (500) status and json
     * content.
     */
    public static Result internalServerError(JSONObject json) {
        return new Result(json.toString()).status(INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param e Throwable content.
     * @return response with internal server error (500) status and throwable
     * content.
     */
    public static Result internalServerError(Throwable e) {
        return new Result().status(INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @return response with not found (404) status.
     */
    public static Result notFound() {
        return new Result().status(NOT_FOUND);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with not found (404) status and input stream content.
     */
    public static Result notFound(InputStream is) {
        return new Result(is).status(NOT_FOUND);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with not found (404) status and bytes content.
     */
    public static Result notFound(byte[] bytes) {
        return new Result(bytes).status(NOT_FOUND);
    }

    /**
     *
     * @param s String content.
     * @return response with not found (404) status and string content.
     */
    public static Result notFound(String s) {
        return new Result(s).status(NOT_FOUND);
    }

    /**
     *
     * @param f File content.
     * @return response with not found (404) status and file content.
     * @throws IOException if file read error
     */
    public static Result notFound(File f) throws IOException {
        return new Result(f).status(NOT_FOUND);
    }

    /**
     *
     * @param json Json content.
     * @return response with not found (404) status and json content.
     */
    public static Result notFound(JSONObject json) {
        return new Result(json.toString()).status(NOT_FOUND);
    }

    /**
     *
     * @return response with unauthorized (401) status.
     */
    public static Result unauthorized() {
        return new Result().status(UNAUTHORIZED);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with unauthorized (401) status and input stream content.
     */
    public static Result unauthorized(InputStream is) {
        return new Result(is).status(UNAUTHORIZED);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with unauthorized (401) status and bytes content.
     */
    public static Result unauthorized(byte[] bytes) {
        return new Result(bytes).status(UNAUTHORIZED);
    }

    /**
     *
     * @param s String content.
     * @return response with unauthorized (401) status and string content.
     */
    public static Result unauthorized(String s) {
        return new Result(s).status(UNAUTHORIZED);
    }

    /**
     *
     * @param f File content.
     * @return response with unauthorized (401) status and file content.
     * @throws IOException if file read error
     */
    public static Result unauthorized(File f) throws IOException {
        return new Result(f).status(UNAUTHORIZED);
    }

    /**
     *
     * @param json Json content.
     * @return response with unauthorized (401) status and json content.
     */
    public static Result unauthorized(JSONObject json) {
        return new Result(json.toString()).status(UNAUTHORIZED);
    }
}
