package com.bunjlabs.fugaframework.foundation;

import com.bunjlabs.fugaframework.foundation.controllers.DefaultController;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Responses {

    public static Response ok() {
        return new Response().setStatus(200);
    }

    public static Response ok(InputStream is) {
        return new Response(is).setStatus(200);
    }

    public static Response ok(byte[] bytes) {
        return new Response(bytes).setStatus(200);
    }

    public static Response ok(String s) {
        return new Response(s).setStatus(200);
    }

    public static Response ok(File f) throws IOException {
        return new Response(f).setStatus(200);
    }

    public static Response badRequest() {
        return ok().setStatus(400);
    }

    public static Response badRequest(InputStream is) {
        return ok(is).setStatus(400);
    }

    public static Response badRequest(byte[] bytes) {
        return ok(bytes).setStatus(400);
    }

    public static Response badRequest(String s) {
        return ok(s).setStatus(400);
    }

    public static Response badRequest(File f) throws IOException {
        return ok(f).setStatus(400);
    }

    public static Response created() {
        return ok().setStatus(201);
    }

    public static Response created(InputStream is) {
        return ok(is).setStatus(201);
    }

    public static Response created(byte[] bytes) {
        return ok(bytes).setStatus(201);
    }

    public static Response created(String s) {
        return ok(s).setStatus(201);
    }

    public static Response created(File f) throws IOException {
        return ok(f).setStatus(201);
    }

    public static Response forbidden() {
        return ok().setStatus(403);
    }

    public static Response forbidden(InputStream is) {
        return ok(is).setStatus(403);
    }

    public static Response forbidden(byte[] bytes) {
        return ok(bytes).setStatus(403);
    }

    public static Response forbidden(String s) {
        return ok(s).setStatus(403);
    }

    public static Response forbidden(File f) throws IOException {
        return ok(f).setStatus(403);
    }

    public static Response nothing() {
        return noContent();
    }

    public static Response noContent() {
        return ok().setStatus(204);
    }

    public static Response found(String url) {
        return ok().setStatus(302).setHeader("Location", url);
    }

    public static Response movedPermanently(String url) {
        return ok().setStatus(301).setHeader("Location", url);
    }

    public static Response redirect(String url) {
        return seeOther(url);
    }

    public static Response seeOther(String url) {
        return ok().setStatus(303).setHeader("Location", url);
    }

    public static Response temporaryRedirect(String url) {
        return ok().setStatus(307).setHeader("Location", url);
    }

    public static Response internalServerError() {
        return ok().setStatus(500);
    }

    public static Response internalServerError(InputStream is) {
        return ok(is).setStatus(500);
    }

    public static Response internalServerError(byte[] bytes) {
        return ok(bytes).setStatus(500);
    }

    public static Response internalServerError(String s) {
        return ok(s).setStatus(500);
    }

    public static Response internalServerError(File f) throws IOException {
        return ok(f).setStatus(500);
    }
    
    public static Response internalServerError(Exception e) {
        StackTraceElement[] ste = e.getStackTrace();

        StringBuilder sb = new StringBuilder();

        for (StackTraceElement el : ste) {
            sb.append(el.toString()).append("\n");
        }
        return ok("<code>" + e.toString() + "</code><br><pre>" + sb.toString() + "</pre>").setStatus(500);
    }

    public static Response notFound() {
        return ok().setStatus(404);
    }

    public static Response notFound(InputStream is) {
        return ok(is).setStatus(404);
    }

    public static Response notFound(byte[] bytes) {
        return ok(bytes).setStatus(404);
    }

    public static Response notFound(String s) {
        return ok(s).setStatus(404);
    }

    public static Response notFound(File f) throws IOException {
        return ok(f).setStatus(404);
    }

    public static Response unauthorized() {
        return ok().setStatus(401);
    }

    public static Response unauthorized(InputStream is) {
        return ok(is).setStatus(401);
    }

    public static Response unauthorized(byte[] bytes) {
        return ok(bytes).setStatus(401);
    }

    public static Response unauthorized(String s) {
        return ok(s).setStatus(401);
    }

    public static Response unauthorized(File f) throws IOException {
        return ok(f).setStatus(401);
    }

    public static Response notFoundDefault(Context ctx) {
        return ok("");//TODO:  DefaultController.notFound(ctx);
    }

}
