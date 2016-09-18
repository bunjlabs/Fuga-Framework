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
package com.bunjlabs.fuga.foundation.http;

public interface Status {

    int CONTINUE = 100;
    int SWITCHING_PROTOCOLS = 101;
    int PROCESSING = 102;

    int OK = 200;
    int CREATED = 201;
    int ACCEPTED = 202;
    int NON_AUTHORITATIVE_INFORMATION = 203;
    int NO_CONTENT = 204;
    int RESET_CONTENT = 205;
    int PARTIAL_CONTENT = 206;
    int MULTI_STATUS = 207;
    int IM_USED = 226;

    int MULTIPLE_CHOICES = 300;
    int MOVED_PERMANENTLY = 301;
    int MOVED_TEMPORARILY = 302;
    int FOUND = 302;
    int SEE_OTHER = 303;
    int NOT_MODIFIED = 304;
    int USE_PROXY = 305;
    int TEMPORARY_REDIRECT = 307;

    int BAD_REQUEST = 400;
    int UNAUTHORIZED = 401;
    int PAYMENT_REQUIRED = 402;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int METHOD_NOT_ALLOWED = 405;
    int NOT_ACCEPTABLE = 406;
    int PROXY_AUTHENTICATION_REQUIRED = 407;
    int REQUEST_TIMEOUT = 408;
    int CONFLICT = 409;
    int GONE = 410;
    int LENGTH_REQUIRED = 411;
    int PRECONDITION_FAILED = 412;
    int REQUEST_ENTITY_TOO_LARGE = 413;
    int REQUEST_URI_TOO_LARGE = 414;
    int UNSUPPORTED_MEDIA_TYPE = 415;
    int REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    int EXPECTATION_FAILED = 417;
    int UNPROCESSABLE_ENTITY = 422;
    int LOCKED = 423;
    int FAILED_DEPENDENCY = 424;
    int UNORDERED_COLLECTION = 425;
    int UPGRADE_REQUIRED = 426;
    int PRECONDITION_REQUIRED = 428;
    int TOO_MANY_REQUESTS = 429;
    int REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
    int REQUESTED_HOST_UNAVAILABLE = 434;
    int RETRY_WITH = 449;
    int UNAVAILABLE_FOR_LEGAL_REASONS = 451;

    int INTERNAL_SERVER_ERROR = 500;
    int NOT_IMPLEMENTED = 501;
    int BAD_GATEWAY = 502;
    int SERVICE_UNAVAILABLE = 503;
    int GATEWAY_TIMEOUT = 504;
    int HTTP_VERSION_NOT_SUPPORTED = 505;
    int VARIANT_ALSO_NEGOTIATES = 506;
    int INSUFFICIENT_STORAGE = 507;
    int LOOP_DETECTED = 508;
    int BANDWIDTH_LIMIT_EXCEEDED = 509;
    int NOT_EXTENDED = 510;
    int NETWORK_AUTHENTICATION_REQUIRED = 511;
}
