package fp.art.stroke.board.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fp.art.stroke.board.model.exception.InsertFailException;
import fp.art.stroke.board.model.service.BoardService;
import fp.art.stroke.board.model.service.ReplyService;
import fp.art.stroke.board.model.vo.Board;
import fp.art.stroke.board.model.vo.BoardDetail;
import fp.art.stroke.board.model.vo.BoardImage;
import fp.art.stroke.board.model.vo.PhotoSmart;
import fp.art.stroke.board.model.vo.Reply;
import fp.art.stroke.common.Util;
import fp.art.stroke.member.controller.MemberController;
import fp.art.stroke.member.model.vo.Member;

@Controller 
@SessionAttributes({"loginMember"}) 
@RequestMapping("/board") 						
public class BoardController {
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private BoardService service;
	@Autowired
	private ReplyService replyService;
	
	
	@GetMapping("/list/{boardCode}")
	public String boardMember(@PathVariable("boardCode") int boardCode,
							  @RequestParam(value = "cp", required = false, defaultValue = "1")int cp,
							  
							  Model model) {
		String path ="";
		Map<String, Object> map = null;
		
		map = service.selectBoardList(cp, boardCode);
		model.addAttribute("map",map);
		
		if (boardCode == 1) {
		    path = "board/memberBoard";
		} else if(boardCode == 2){
		    path = "board/memberBoard";
		}
		
		return path;
	}
	//insert와 update를 하기위해 스마트에디터로이동
	@GetMapping("/boardWrite/{boardCode}")
	public String boardWrite(@PathVariable("boardCode") int boardCode,
							 @RequestParam(value = "type", required = false, defaultValue = "1")String type,
							 @RequestParam(value="no", required = false, defaultValue = "0")int boardId,
							 Model model) {
		 
		if(type.equals("update")) {
			
			BoardDetail detail = service.selectBoardDetail(boardId);
			detail.setBoardContent(Util.newLineClear(detail.getBoardContent()));
			logger.info(detail.getBoardContent());
			model.addAttribute("detail",detail);
		}
		
		return "board/boardWrite";
	}
	@GetMapping("/detail/{boardCode}/{boardId}")
	public String boardDetail(@PathVariable("boardCode") int boardCode,
							  @PathVariable("boardId")int boardId,
							  @RequestParam(value = "cp", required = false, defaultValue = "1")int cp,
							  HttpSession session,
								HttpServletRequest req, HttpServletResponse resp,
							  Model model) {
		int result = 0;
		Map<String,Object> map = new HashMap();
		BoardDetail detail = service.selectBoardDetail(boardId);
		List<Reply> reply = replyService.selectReplyList(boardId);
		map.put("replyList", reply);
		map.put("detail", detail);

		
		model.addAttribute("map",map);
		return "board/boardDetail";
	}
	
	@GetMapping("/detailWriter")
	public String boardDetailWriter() {
		return "board/boardWriterDetail";
	}
	@GetMapping("/boardBoard")
	public String boardBoard() {
		return "board/boardBoard";
	}
	@GetMapping("/boardBoardDetail")
	public String boardBoardDetail() {
		return "board/boardBoardDetail";
	}
	//작가 종합페이지 이동
	@GetMapping("/writer")
	public String selectWriter(@RequestParam(value = "cp", required = false, defaultValue = "1")int cp,
							   Model model) {
		Map<String, Object> map = service.selectWriter(cp);
		model.addAttribute("map",map);
		return "board/boardWriter";
	}
	//작가 상세페이지 이동
	@GetMapping("/detailWriter/{memberId}")
	public String detailWriter(@PathVariable("memberId") int memberId,
							   Model model) {
		
		Map<String, Object> map = service.selectWriterDetail(memberId);
		model.addAttribute("map",map);
		return "board/boardWriterDetail";
	}
	//쪽지보내기
	@PostMapping("/detailWriter/{memberId}/sendLetter")
	public String SendWriterLetter(@PathVariable int memberId,
								   RedirectAttributes ra,	
								   @RequestParam(value = "writerName")String writerName,
								   @RequestParam(value = "sendName")String sendName,
								   @RequestParam(value = "sendTitle")String sendTitle,
								   @RequestParam(value = "sendText")String sendText
								   ) {
		
		int result = 0;
		String message="";
		result = service.sendLetter(memberId,writerName,sendName,sendTitle,sendText);
		
		if(result != 1) {
			message="존재하지 않는 작가입니다.";
			ra.addFlashAttribute("message",message);
		}
		return "redirect:/board/detailWriter/"+memberId;
	}
	
		
	

	
	//사진 단일 업로드
	@PostMapping("/write/img")
	public String photoUpload(HttpServletRequest request, PhotoSmart vo) {
		String callback = vo.getCallback();
		String callback_func = vo.getCallback_func();
		String file_result = "";
		
		try {
			if(vo.getFiledata() != null && vo.getFiledata().getOriginalFilename()!=null && ! vo.getFiledata().getOriginalFilename().equals("")) {
				//파일이 존재하면
				String original_name = vo.getFiledata().getOriginalFilename();
				String ext = original_name.substring(original_name.lastIndexOf(".")+1);
				//파일 기본경로
				String defaultPath = request.getSession().getServletContext().getRealPath("/");
				//파일 기본경로_상세경로
				String path = defaultPath + "resources" + File.separator + "images" + File.separator +"boardImg" + File.separator;
				File file = new File(path);
				
				if(!file.exists()) {
					file.mkdirs();
				}
				String realname = UUID.randomUUID().toString() + "." + ext;
				
				vo.getFiledata().transferTo(new File(path+realname));
				file_result += "&bNewLine=true&sFileName=" +original_name + "&sFileURL=/comm/resources/images/boardImg/"+realname;
				
			}else {
				file_result+= "&errstr=error";
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + callback + "?callback_func="+callback_func+file_result;
	}
	//사진 멀티 업로드
	@PostMapping("/multiphotoUpload")
	public void multiplePhotoUpload(HttpServletRequest request, HttpServletResponse response) {
		try {
			String sFileInfo = "";
			
			String filename = request.getHeader("file-name");
			String filename_ext = filename.substring(filename.lastIndexOf(".")+1);
			
			filename_ext = filename_ext.toLowerCase();
			
			String dftFilePath = request.getSession().getServletContext().getRealPath("/");
			String filePath = dftFilePath + "resources" + File.separator + "images" + File.separator+"boardImg" +File.separator;
			File file = new File(filePath);
			if(!file.exists()) {
				file.mkdirs();
			}
			String realFileNm = "";
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyHHmmss");
			String today = formatter.format(new java.util.Date());
			realFileNm = today + UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
			String rlFileNm = filePath + realFileNm;
			
			//서버에 파일쓰기
			
			InputStream is = request.getInputStream();
			OutputStream os = new FileOutputStream(rlFileNm);
			int numRead;
			byte b[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
			while((numRead = is.read(b,0,b.length)) != -1) {
				os.write(b,0,numRead);
			}
			if(is != null) {
				is.close();
			}
			os.flush();
			os.close();
			sFileInfo+="&bNewLine=true";
			sFileInfo += "&sFileName="+ filename;;
			sFileInfo += "&sFileURL=" + "/comm/resources/images/boardImg/"+realFileNm;
			PrintWriter print = response.getWriter();
			print.print(sFileInfo);
			print.flush();
			print.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	//게시물 등록 및 수정
	@PostMapping("/write/{boardCode}")
	public String writeBoard(@PathVariable int boardCode,
							 @RequestParam String title,
							 @RequestParam String smartEditor,
							 @RequestParam(value = "type", required=false, defaultValue = "insert")String type,
							 @RequestParam(value = "no", required=false, defaultValue = "0")int boardId,
							 HttpSession session) {
			
			Member loginMember = (Member)session.getAttribute("loginMember");
			int memberId = loginMember.getMemberId();
			String memberNick = loginMember.getMemberNick();
			int result = service.writeBoard(boardCode,title,smartEditor,memberId,memberNick,type,boardId);
			
			String path="";
			
				path = "redirect:/board/list/"+boardCode;
			
		return path;
	}
	
	@PostMapping("/delete/{boardCode}")
	public String deleteBoard(@PathVariable int boardCode,
							  @RequestParam(value = "no")int no){
		int result = service.deleteBoard(boardCode,no);
		String path ="";
		if(result ==1) {
			path = "'redirect:/board/list/'+ boardCode";
		}else {
			
		}
		return "redirect:/board/list/"+boardCode;
	}
	
	
	
	
		
}
				
