package fp.art.stroke.member.model.service;

import fp.art.stroke.board.model.vo.BoardDetail;
import fp.art.stroke.member.model.vo.Follow;
import fp.art.stroke.member.model.vo.Member;

public interface MemberService {

	/**로그인서비스
	 * @param inputMember
	 * @return loginMember
	 */

	
	public abstract Member login(Member inputMember);

	public abstract int emailDupCheck(String memberEmail);

	public abstract int nicknameDupCheck(String memberNick);

	public abstract int signUp(Member inputMember);
	
//06/12 ey
	public abstract int insertCertification(String inputEmail, String cNumber);

	public abstract int checkNumber(String inputEmail, String cNumber);
	
	public abstract int telInsertCertification(String inputTel, String smsCNumber);

	public abstract int checkSmsNumber(String inputTel, String smsCNumber);

	public abstract int insertFollow(Follow follow);

	public abstract int deleteFollow(Follow follow);



	

	



}
