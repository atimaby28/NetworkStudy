package com.atimaby.chatserver.member.controller;

import com.atimaby.chatserver.common.auth.JwtTokenProvider;
import com.atimaby.chatserver.member.domain.Member;
import com.atimaby.chatserver.member.dto.MemberListResDto;
import com.atimaby.chatserver.member.dto.MemberLoginReqDto;
import com.atimaby.chatserver.member.dto.MemberSaveReqDto;
import com.atimaby.chatserver.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<?> memberCreate(@RequestBody MemberSaveReqDto memberSaveReqDto) {
        Member member = memberService.create(memberSaveReqDto);

        return new ResponseEntity<>(member.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/doLogin")
    public ResponseEntity<?> doLogin(@RequestBody MemberLoginReqDto memberLoginReqDto) {
        // email, password 검증
        Member member = memberService.login(memberLoginReqDto);

        // 일치할 경우 access token 발행
        String jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("id", member.getId());
        loginInfo.put("token", jwtToken);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }

    @GetMapping("list")
    public ResponseEntity<?> memberList() {
        List<MemberListResDto> dtos = memberService.findAll();

        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
