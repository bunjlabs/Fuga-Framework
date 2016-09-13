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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

/**
 * Helper class that contains useful methods to generate responses.
 */
public class Responses {

    /**
     *
     * @return response with ok (200) status.
     */
    public static Response ok() {
        return new Response().status(200);
    }

    /**
     *
     * @param is InputStream content.
     * @return response with ok (200) status and input stream content.
     */
    public static Response ok(InputStream is) {
        return new Response(is).status(200);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with ok (200) status and bytes content.
     */
    public static Response ok(byte[] bytes) {
        return new Response(bytes).status(200);
    }

    /**
     *
     * @param s String content
     * @return response with ok (200) status and string content.
     */
    public static Response ok(String s) {
        return new Response(s).status(200);
    }

    /**
     *
     * @param f File content.
     * @return response with ok (200) status and file content.
     * @throws IOException
     */
    public static Response ok(File f) throws IOException {
        return new Response(f).status(200);
    }

    /**
     *
     * @param json Json content.
     * @return response with ok (200) status and json content.
     */
    public static Response ok(JSONObject json) {
        return new Response(json.toString()).asJson().status(200);
    }

    /**
     *
     * @return response with bad request (400) status.
     */
    public static Response badRequest() {
        return ok().status(400);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with bad request (400) status and input stream content.
     */
    public static Response badRequest(InputStream is) {
        return ok(is).status(400);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with bad request (400) status and bytes content.
     */
    public static Response badRequest(byte[] bytes) {
        return ok(bytes).status(400);
    }

    /**
     *
     * @param s String content.
     * @return response with bad request (400) status and string content.
     */
    public static Response badRequest(String s) {
        return ok(s).status(400);
    }

    /**
     *
     * @param f File content.
     * @return response with bad request (400) status and file content.
     * @throws IOException
     */
    public static Response badRequest(File f) throws IOException {
        return ok(f).status(400);
    }

    /**
     *
     * @param json Json content.
     * @return response with bad request (400) status and json content.
     */
    public static Response badRequest(JSONObject json) {
        return ok(json).status(400);
    }

    /**
     *
     * @return response with created (201) status.
     */
    public static Response created() {
        return ok().status(201);
    }

    /**
     *
     * @param is InputStream content.
     * @return response with created (201) status and input stream content.
     */
    public static Response created(InputStream is) {
        return ok(is).status(201);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with created (201) status and bytes content.
     */
    public static Response created(byte[] bytes) {
        return ok(bytes).status(201);
    }

    /**
     *
     * @param s String content.
     * @return response with created (201) status and string content.
     */
    public static Response created(String s) {
        return ok(s).status(201);
    }

    /**
     *
     * @param f File content.
     * @return response with created (201) status and file content.
     * @throws IOException
     */
    public static Response created(File f) throws IOException {
        return ok(f).status(201);
    }

    /**
     *
     * @param json Json content.
     * @return response with created (201) status and json content.
     */
    public static Response created(JSONObject json) {
        return ok(json).status(201);
    }

    /**
     *
     * @return response with forbidden (403) status.
     */
    public static Response forbidden() {
        return ok().status(403);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with forbidden (403) status and input stream content.
     */
    public static Response forbidden(InputStream is) {
        return ok(is).status(403);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with forbidden (403) status and bytes content.
     */
    public static Response forbidden(byte[] bytes) {
        return ok(bytes).status(403);
    }

    /**
     *
     * @param s String content.
     * @return response with forbidden (403) status and string content.
     */
    public static Response forbidden(String s) {
        return ok(s).status(403);
    }

    /**
     *
     * @param f File content.
     * @return response with forbidden (403) status and file content.
     * @throws IOException
     */
    public static Response forbidden(File f) throws IOException {
        return ok(f).status(403);
    }

    /**
     *
     * @param json Json content.
     * @return response with forbidden (403) status and json content.
     */
    public static Response forbidden(JSONObject json) {
        return ok(json).status(403);
    }

    /**
     *
     * @return response with no content (204) status.
     */
    public static Response nothing() {
        return noContent();
    }

    /**
     *
     * @return response with no content (204) status.
     */
    public static Response noContent() {
        return ok().status(204);
    }

    /**
     *
     * @param url Location url.
     * @return response with found (302) status and location url.
     */
    public static Response found(String url) {
        return ok().status(302).header("Location", url);
    }

    /**
     *
     * @param url Location url.
     * @return response with moved permanently (301) status and location url.
     */
    public static Response movedPermanently(String url) {
        return ok().status(301).header("Location", url);
    }

    /**
     *
     * @param url Location url.
     * @return response with see other (303) status and location url.
     */
    public static Response redirect(String url) {
        return seeOther(url);
    }

    /**
     *
     * @param url Location url.
     * @return response with see other (303) status and location url.
     */
    public static Response seeOther(String url) {
        return ok().status(303).header("Location", url);
    }

    /**
     *
     * @param url Location url.
     * @return response with temporary redirect (307) status and location url.
     */
    public static Response temporaryRedirect(String url) {
        return ok().status(307).header("Location", url);
    }

    /**
     *
     * @return response with internal server error (500) status.
     */
    public static Response internalServerError() {
        return ok().status(500);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with internal server error (500) status and input stream
     * content.
     */
    public static Response internalServerError(InputStream is) {
        return ok(is).status(500);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with internal server error (500) status and bytes
     * content.
     */
    public static Response internalServerError(byte[] bytes) {
        return ok(bytes).status(500);
    }

    /**
     *
     * @param s String content.
     * @return response with internal server error (500) status and string
     * content.
     */
    public static Response internalServerError(String s) {
        return ok(s).status(500);
    }

    /**
     *
     * @param f File content.
     * @return response with internal server error (500) status and file
     * content.
     * @throws IOException
     */
    public static Response internalServerError(File f) throws IOException {
        return ok(f).status(500);
    }

    /**
     *
     * @param json Json content.
     * @return response with internal server error (500) status and json
     * content.
     */
    public static Response internalServerError(JSONObject json) {
        return ok(json).status(500);
    }

    /**
     *
     * @param e Throwable content.
     * @return response with internal server error (500) status and throwable
     * content.
     */
    public static Response internalServerError(Throwable e) {
        return ok().status(500);
    }

    /**
     *
     * @return response with not found (404) status.
     */
    public static Response notFound() {
        return ok().status(404);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with not found (404) status and input stream content.
     */
    public static Response notFound(InputStream is) {
        return ok(is).status(404);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with not found (404) status and bytes content.
     */
    public static Response notFound(byte[] bytes) {
        return ok(bytes).status(404);
    }

    /**
     *
     * @param s String content.
     * @return response with not found (404) status and string content.
     */
    public static Response notFound(String s) {
        return ok(s).status(404);
    }

    /**
     *
     * @param f File content.
     * @return response with not found (404) status and file content.
     * @throws IOException
     */
    public static Response notFound(File f) throws IOException {
        return ok(f).status(404);
    }

    /**
     *
     * @param json Json content.
     * @return response with not found (404) status and json content.
     */
    public static Response notFound(JSONObject json) {
        return ok(json).status(404);
    }

    /**
     *
     * @return response with unauthorized (401) status.
     */
    public static Response unauthorized() {
        return ok().status(401);
    }

    /**
     *
     * @param is Input stream content.
     * @return response with unauthorized (401) status and input stream content.
     */
    public static Response unauthorized(InputStream is) {
        return ok(is).status(401);
    }

    /**
     *
     * @param bytes Byte array content.
     * @return response with unauthorized (401) status and bytes content.
     */
    public static Response unauthorized(byte[] bytes) {
        return ok(bytes).status(401);
    }

    /**
     *
     * @param s String content.
     * @return response with unauthorized (401) status and string content.
     */
    public static Response unauthorized(String s) {
        return ok(s).status(401);
    }

    /**
     *
     * @param f File content.
     * @return response with unauthorized (401) status and file content.
     * @throws IOException
     */
    public static Response unauthorized(File f) throws IOException {
        return ok(f).status(401);
    }

    /**
     *
     * @param json Json content.
     * @return response with unauthorized (401) status and json content.
     */
    public static Response unauthorized(JSONObject json) {
        return ok(json).status(401);
    }
}
