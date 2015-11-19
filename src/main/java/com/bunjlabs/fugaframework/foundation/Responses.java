package com.bunjlabs.fugaframework.foundation;

import com.bunjlabs.fugaframework.foundation.controllers.DefaultController;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Responses {

    protected static Response ok() {
        return new Response();
    }

    protected static Response ok(InputStream is) {
        return new Response(is);
    }

    protected static Response ok(byte[] bytes) {
        return new Response(bytes);
    }

    protected static Response ok(String s) {
        return new Response(s);
    }

    protected static Response ok(File f) throws IOException {
        return new Response(f);
    }

    protected static Response badRequest() {
        return ok().setStatus(400);
    }

    protected static Response badRequest(InputStream is) {
        return ok(is).setStatus(400);
    }

    protected static Response badRequest(byte[] bytes) {
        return ok(bytes).setStatus(400);
    }

    protected static Response badRequest(String s) {
        return ok(s).setStatus(400);
    }

    protected static Response badRequest(File f) throws IOException {
        return ok(f).setStatus(400);
    }

    protected static Response created() {
        return ok().setStatus(201);
    }

    protected static Response created(InputStream is) {
        return ok(is).setStatus(201);
    }

    protected static Response created(byte[] bytes) {
        return ok(bytes).setStatus(201);
    }

    protected static Response created(String s) {
        return ok(s).setStatus(201);
    }

    protected static Response created(File f) throws IOException {
        return ok(f).setStatus(201);
    }

    protected static Response forbidden() {
        return ok().setStatus(403);
    }

    protected static Response forbidden(InputStream is) {
        return ok(is).setStatus(403);
    }

    protected static Response forbidden(byte[] bytes) {
        return ok(bytes).setStatus(403);
    }

    protected static Response forbidden(String s) {
        return ok(s).setStatus(403);
    }

    protected static Response forbidden(File f) throws IOException {
        return ok(f).setStatus(403);
    }

    protected static Response nothing() {
        return noContent();
    }

    protected static Response noContent() {
        return ok().setStatus(204);
    }

    protected static Response found(String url) {
        return ok().setStatus(302).setHeader("Location", url);
    }

    protected static Response movedPermanently(String url) {
        return ok().setStatus(301).setHeader("Location", url);
    }

    protected static Response redirect(String url) {
        return seeOther(url);
    }

    protected static Response seeOther(String url) {
        return ok().setStatus(303).setHeader("Location", url);
    }

    protected static Response temporaryRedirect(String url) {
        return ok().setStatus(307).setHeader("Location", url);
    }

    protected static Response internalServerError() {
        return ok().setStatus(500);
    }

    protected static Response internalServerError(InputStream is) {
        return ok(is).setStatus(500);
    }

    protected static Response internalServerError(byte[] bytes) {
        return ok(bytes).setStatus(500);
    }

    protected static Response internalServerError(String s) {
        return ok(s).setStatus(500);
    }

    protected static Response internalServerError(File f) throws IOException {
        return ok(f).setStatus(500);
    }

    protected static Response notFound() {
        return ok().setStatus(404);
    }

    protected static Response notFound(InputStream is) {
        return ok(is).setStatus(404);
    }

    protected static Response notFound(byte[] bytes) {
        return ok(bytes).setStatus(404);
    }

    protected static Response notFound(String s) {
        return ok(s).setStatus(404);
    }

    protected static Response notFound(File f) throws IOException {
        return ok(f).setStatus(404);
    }

    protected static Response unauthorized() {
        return ok().setStatus(401);
    }

    protected static Response unauthorized(InputStream is) {
        return ok(is).setStatus(401);
    }

    protected static Response unauthorized(byte[] bytes) {
        return ok(bytes).setStatus(401);
    }

    protected static Response unauthorized(String s) {
        return ok(s).setStatus(401);
    }

    protected static Response unauthorized(File f) throws IOException {
        return ok(f).setStatus(401);
    }

    protected static Response notFoundDefault(Context ctx) {
        return DefaultController.notFound(ctx);
    }

}
