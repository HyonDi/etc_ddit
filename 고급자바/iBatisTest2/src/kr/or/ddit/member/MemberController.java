package kr.or.ddit.member;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Pagination;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.member.vo.MemberVO;

public class MemberController implements Initializable{

	@FXML private Button addBtn;
	@FXML private Button editBtn;
	@FXML private Button delBtn;
	@FXML private TableView<MemberVO> tbView;
	@FXML private TableColumn<MemberVO,String> idCol;
	@FXML private TableColumn<MemberVO,String> nameCol;
	@FXML private TableColumn<MemberVO,String> telCol;
	@FXML private TableColumn<MemberVO,String> addrCol;
	@FXML private Pagination pagnation;
	private Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	// 페이지네이션 변수
	private int from, to, itemsForPage;
	
	// 정보 담을 그릇
	private ObservableList<MemberVO> allTableData, currentPageData;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		pickData();
	
	}
	
	
	public void pickData() {
		idCol.setCellValueFactory(new PropertyValueFactory<MemberVO,String>("mem_id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<MemberVO,String>("mem_name"));
		telCol.setCellValueFactory(new PropertyValueFactory<MemberVO,String>("mem_tel"));
		addrCol.setCellValueFactory(new PropertyValueFactory<MemberVO,String>("mem_addr"));
		
		MemberServiceImpl service = MemberServiceImpl.getInstance();
		List<MemberVO> list2 = service.getAllMemberList(); // db에서 가져온 진짜데이터
		
		ObservableList<MemberVO> list = FXCollections.observableArrayList(list2);// 옵저버블에 넣어옴.
		
		// 전체 테이블 데이터 설정
		tbView.setItems(list);// 테이블뷰에 보이기.
		
		
		allTableData = FXCollections.observableArrayList(list); // 그냥 list넣으면 안돼.
		itemsForPage = 10;
		int totPageCount = allTableData.size()%itemsForPage ==0 ?allTableData.size()/itemsForPage : allTableData.size()/itemsForPage +1;
		System.out.println(totPageCount + "!!!!!!!!!!!!!!!!!!");
		pagnation.setPageCount(totPageCount);
		
		
		pagnation.setPageFactory(new Callback<Integer, Node>() {
			@Override
			public Node call(Integer pageindex) {
				from = pageindex * itemsForPage;
				to = from + itemsForPage -1;
				tbView.setItems(getTableViewData(from, to));
				
				return tbView;
			}
		});
	}
	
	
	
	private ObservableList<MemberVO> getTableViewData(int from, int to){
		
		// 현재 페이지의 데이터 초기화
		currentPageData = FXCollections.observableArrayList();
		
		int totSize = allTableData.size();
		for(int i = from; i <= to && i < totSize; i ++) {
			currentPageData.add(allTableData.get(i));
		}
		
		return currentPageData;
	}
	
	//-------------------------------------------------------------------------------------------
	
	Parent parent = null;
	
	/*
	@FXML TextField id;
	@FXML TextField name;
	@FXML TextField tel;
	@FXML TextField addr;
	@FXML Button okBtn;
	@FXML Button cancelBtn;*/
	
	@FXML public void addBtnClicked() {
		
		
		
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.setTitle("추가");
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(primaryStage);
		
		// 4. 자식창이 나타날 컨테이너 객체 생성
		
		
		

		try {
			parent = FXMLLoader.load(getClass().getResource("MemberAdd.fxml"));
			
			
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		// 5. Scene객체 생성해서 컨테이너 객체 추가
		Scene scene = new Scene(parent);
		
		
		// 6. Stage객체에 Scene객체 추가
		dialog.setScene(scene);
		dialog.show();
		
		TextField id = (TextField) parent.lookup("#id");
		TextField name = (TextField) parent.lookup("#name");
		TextField tel = (TextField) parent.lookup("#tel");
		TextField addr = (TextField) parent.lookup("#addr");
		
		

		Button okBtn = (Button) parent.lookup("#okBtn");
		
		okBtn.setOnAction(e->{
			MemberServiceImpl service = MemberServiceImpl.getInstance();
			MemberVO mv = new MemberVO();
			
			mv.setMem_id(id.getText());
			mv.setMem_name(name.getText());
			mv.setMem_tel(tel.getText());
			mv.setMem_addr(addr.getText());
		
			
			service.insertMember(mv);
			
			pickData();
			
			dialog.close();
			
			//메인화면의 테이블의 데이터를 추가
			
			//Platform.exit(); // 메인창닫는거임. 여기선이거안쓸거야! 부모창닫기!
			
		});
	}

	@FXML public void editBtnClicked(ActionEvent event) {}

	@FXML public void delBtnClicked(ActionEvent event) {
		
	}


}
