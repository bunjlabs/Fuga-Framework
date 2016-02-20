package com.bunjlabs.fugaframework.foundation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Responses {

    public static Response ok() {
        return new Response().status(200);
    }

    public static Response ok(InputStream is) {
        return new Response(is).status(200);
    }

    public static Response ok(byte[] bytes) {
        return new Response(bytes).status(200);
    }

    public static Response ok(String s) {
        return new Response(s).status(200);
    }

    public static Response ok(File f) throws IOException {
        return new Response(f).status(200);
    }

    public static Response badRequest() {
        return ok().status(400);
    }

    public static Response badRequest(InputStream is) {
        return ok(is).status(400);
    }

    public static Response badRequest(byte[] bytes) {
        return ok(bytes).status(400);
    }

    public static Response badRequest(String s) {
        return ok(s).status(400);
    }

    public static Response badRequest(File f) throws IOException {
        return ok(f).status(400);
    }

    public static Response created() {
        return ok().status(201);
    }

    public static Response created(InputStream is) {
        return ok(is).status(201);
    }

    public static Response created(byte[] bytes) {
        return ok(bytes).status(201);
    }

    public static Response created(String s) {
        return ok(s).status(201);
    }

    public static Response created(File f) throws IOException {
        return ok(f).status(201);
    }

    public static Response forbidden() {
        return ok().status(403);
    }

    public static Response forbidden(InputStream is) {
        return ok(is).status(403);
    }

    public static Response forbidden(byte[] bytes) {
        return ok(bytes).status(403);
    }

    public static Response forbidden(String s) {
        return ok(s).status(403);
    }

    public static Response forbidden(File f) throws IOException {
        return ok(f).status(403);
    }

    public static Response nothing() {
        return noContent();
    }

    public static Response noContent() {
        return ok().status(204);
    }

    public static Response found(String url) {
        return ok().status(302).header("Location", url);
    }

    public static Response movedPermanently(String url) {
        return ok().status(301).header("Location", url);
    }

    public static Response redirect(String url) {
        return seeOther(url);
    }

    public static Response seeOther(String url) {
        return ok().status(303).header("Location", url);
    }

    public static Response temporaryRedirect(String url) {
        return ok().status(307).header("Location", url);
    }

    public static Response internalServerError() {
        return ok().status(500);
    }

    public static Response internalServerError(InputStream is) {
        return ok(is).status(500);
    }

    public static Response internalServerError(byte[] bytes) {
        return ok(bytes).status(500);
    }

    public static Response internalServerError(String s) {
        return ok(s).status(500);
    }

    public static Response internalServerError(File f) throws IOException {
        return ok(f).status(500);
    }

    public static Response internalServerError(Throwable e) {
        StackTraceElement[] ste = e.getStackTrace();

        StringBuilder sb = new StringBuilder();

        sb.append("<h2>").append(e.toString()).append("</h2><p>");
        for (StackTraceElement el : ste) {
            sb.append(el.toString()).append("<br>");
        }
        sb.append("</p>");

        Throwable cause = e.getCause();
        if (cause != null) {
            ste = cause.getStackTrace();
            sb.append("<h3>Caused by: ").append(cause.toString()).append("</h3><p>");
            for (StackTraceElement el : ste) {
                sb.append(el.toString()).append("<br>");
            }
            sb.append("</p>");
        }

        return ok(sb.toString()).status(500);
    }

    public static Response notFound() {
        return ok().status(404);
    }

    public static Response notFound(InputStream is) {
        return ok(is).status(404);
    }

    public static Response notFound(byte[] bytes) {
        return ok(bytes).status(404);
    }

    public static Response notFound(String s) {
        return ok(s).status(404);
    }

    public static Response notFound(File f) throws IOException {
        return ok(f).status(404);
    }

    public static Response unauthorized() {
        return ok().status(401);
    }

    public static Response unauthorized(InputStream is) {
        return ok(is).status(401);
    }

    public static Response unauthorized(byte[] bytes) {
        return ok(bytes).status(401);
    }

    public static Response unauthorized(String s) {
        return ok(s).status(401);
    }

    public static Response unauthorized(File f) throws IOException {
        return ok(f).status(401);
    }
}
