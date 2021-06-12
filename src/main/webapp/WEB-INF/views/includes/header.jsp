<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>

<!-- Bootstrap Core CSS -->
<link href="/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

</head>

<header class="p-3 mb-2 bg-success text-white">
  <div class="container">
    <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
      <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
        <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap"><use xlink:href="#bootstrap"/></svg>
      </a>

      <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
        <li><a href="/board/publicBikeParking" class="nav-link px-2 text-white">메인</a></li>
        <li><a href="/board/breakdownList" class="nav-link px-2 text-white">고장신고</a></li>
        <li><a href="/board/neglect" class="nav-link px-2 text-white">방치신고</a></li>
        <li><a href="/board/question" class="nav-link px-2 text-white">문의하기</a></li>
      </ul>

      <form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
        <input type="search" class="form-control form-control-dark" placeholder="Search..." aria-label="Search">
      </form>

      <div class="text-end">
        <button type="button" class="btn btn-outline-light me-2">Login</button>
        <button type="button" class="btn btn-warning">Sign-up</button>
      </div>
    </div>
  </div>
</header>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>