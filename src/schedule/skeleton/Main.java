package schedule.skeleton;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import schedule.io.BigLittleConverter;
import schedule.io.ReadFromLocal;
import schedule.io.Write2Local;
import schedule.model.TimeTable;
import schedule.station.FoldHeadController;
import schedule.viewmodel.TimeTableVM;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;

public class Main extends Application {

	private double x = 0.00;
	private double y = 0.00;
	private double width = 0.00;
	private double height = 0.00;
	private boolean isMax = false;
	private boolean isRight;// 是否处于右边界调整窗口状态
	private boolean isBottomRight;// 是否处于右下角调整窗口状态
	private boolean isBottom;// 是否处于下边界调整窗口状态
	private double RESIZE_WIDTH = 5.00;
	private double MIN_WIDTH = 400.00;
	private double MIN_HEIGHT = 300.00;
	private double xOffset = 0, yOffset = 0;// 自定义dialog移动横纵坐标

	public static FXMLLoader loader;
	private TimeTableVM timeTableVM;
	private boolean timeTableReceiver = true;
	private DatagramSocket ds0;

	@Override
	public void start(Stage primaryStage) {
		try {
			loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			BorderPane root = (BorderPane) loader.load();

			// Scene scene = new Scene(setCustomWindow(primaryStage, root), 1680, 1050);
			Scene scene = new Scene(root, 1680, 1050);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("车载列车运行图可视化");
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("app_icon.PNG")));
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent event) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								Write2Local.save2Disk(timeTableVM.timeTable.path, timeTableVM.timeTable);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();

					// 关闭所有窗口
					Platform.exit();

					// 关闭所有后台线程
					closeBackEndTasks();
				}
			});
			primaryStage.show();

			initialDataAndUI(loader.getController());
			initialBackEndTasks();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 运行后台任务
	private void initialBackEndTasks() {
		try {
			String port = ReadFromLocal.getPath(5);
			ds0 = new DatagramSocket(Integer.valueOf(port));
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean newHead = false;
					int newDataLen = 0;
					int typeCode = 0;
					while (timeTableReceiver) {
						try {
							if (typeCode == 0) {
								byte[] buf = new byte[4];
								DatagramPacket dp0 = new DatagramPacket(buf, buf.length);
								ds0.receive(dp0);
								typeCode = BigLittleConverter.byteArrToInt(dp0.getData());
							} else {
								// 接收同步时刻表
								if (typeCode == 1) {
									if (!newHead) {
										byte[] buf = new byte[4];
										DatagramPacket dp = new DatagramPacket(buf, buf.length);
										ds0.receive(dp);
										newDataLen = BigLittleConverter.byteArrToInt(dp.getData());
										newHead = true;
									} else {
										Write2Local.save2Disk(timeTableVM.timeTable.path, timeTableVM.timeTable);

										byte[] buf = new byte[newDataLen];
										DatagramPacket dp = new DatagramPacket(buf, buf.length);
										ds0.receive(dp);
										ByteArrayInputStream buffers = new ByteArrayInputStream(dp.getData());
										ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(buffers));

										TimeTable timeTable = (TimeTable) in.readObject();
										timeTable.path = null;

										Platform.runLater(() -> {
											try {
												((MainController) (Main.loader.getController()))
														.refreshMap(new TimeTableVM(timeTable));
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										});

										in.close();
										buffers.close();
										newHead = false;
										typeCode = 0;
									}
								}

								// 接收时刻表计划
								if (typeCode == 3) {
									if (!newHead) {
										byte[] buf = new byte[4];
										DatagramPacket dp = new DatagramPacket(buf, buf.length);
										ds0.receive(dp);
										newDataLen = BigLittleConverter.byteArrToInt(dp.getData());
										newHead = true;
									} else {
										byte[] buf = new byte[newDataLen];
										DatagramPacket dp = new DatagramPacket(buf, buf.length);
										ds0.receive(dp);
										ByteArrayInputStream buffers = new ByteArrayInputStream(dp.getData());
										ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(buffers));

										TimeTable timeTable = (TimeTable) in.readObject();
										DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
										Write2Local.save2Disk(dtf.format(LocalDateTime.now()) + ".xml", timeTable);

										Platform.runLater(() -> {
											try {
												Stage alert = new Stage();
												FXMLLoader loader = new FXMLLoader(
														getClass().getResource("AlertScene.fxml"));
												AnchorPane root;
												try {
													root = (AnchorPane) loader.load();
													Scene scene = new Scene(root);
													scene.getStylesheets().add(
															getClass().getResource("application.css").toExternalForm());
													alert.setScene(scene);
													alert.setResizable(false);
													alert.setTitle("收到运行计划");
													alert.getIcons().add(
															new Image(getClass().getResourceAsStream("app_icon.PNG")));
													AlertSceneController controller = loader.getController();
													controller.setContent("收到时刻表，已保存当前目录！");
													alert.show();
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											} catch (Exception e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										});

										in.close();
										buffers.close();
										newHead = false;
										typeCode = 0;
									}
								}
								// 接收调度命令
								if (typeCode == 2) {
									byte[] buf = new byte[1024 * 5];
									DatagramPacket dp = new DatagramPacket(buf, buf.length);
									ds0.receive(dp);

									String msg = new String(buf).trim();

									Platform.runLater(() -> {
										try {
											((MainController) (Main.loader.getController())).notifyMsg(msg);
											;
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									});

									typeCode = 0;
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeBackEndTasks() {
		// 定时发送关闭
		UpdateTimeController.timer.cancel();
		// 时刻表接收端口关闭
		timeTableReceiver = false;

		ds0.close();
	}

	private GridPane setCustomWindow(Stage primaryStage, BorderPane root) {
		GridPane gpTitle = new GridPane();
		gpTitle.setAlignment(Pos.CENTER_LEFT);
		gpTitle.setPadding(new Insets(5));
		Label lbTitle = new Label("");
		Button btnMin = new Button("一");
		Button btnMax = new Button("口");
		Button btnClose = new Button("X");
		gpTitle.add(lbTitle, 0, 0);
		gpTitle.add(btnMin, 1, 0);
		gpTitle.add(btnMax, 2, 0);
		gpTitle.add(btnClose, 3, 0);
		gpTitle.add(root, 0, 1, 4, 1);
		GridPane.setHgrow(lbTitle, Priority.ALWAYS);
		GridPane.setHgrow(root, Priority.ALWAYS);
		GridPane.setVgrow(root, Priority.ALWAYS);

		btnMin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				primaryStage.setIconified(true);
			}
		});
		btnMax.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Rectangle2D rectangle2d = Screen.getPrimary().getVisualBounds();
				isMax = !isMax;
				if (isMax) {
					// 最大化
					primaryStage.setX(rectangle2d.getMinX());
					primaryStage.setY(rectangle2d.getMinY());
					primaryStage.setWidth(rectangle2d.getWidth());
					primaryStage.setHeight(rectangle2d.getHeight());
				} else {
					// 缩放回原来的大小
					primaryStage.setX(x);
					primaryStage.setY(y);
					primaryStage.setWidth(width);
					primaryStage.setHeight(height);
				}
			}
		});
		btnClose.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				primaryStage.close();
			}
		});
		primaryStage.xProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null && !isMax) {
					x = newValue.doubleValue();
				}
			}
		});
		primaryStage.yProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null && !isMax) {
					y = newValue.doubleValue();
				}
			}
		});
		primaryStage.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null && !isMax) {
					width = newValue.doubleValue();
				}
			}
		});
		primaryStage.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue != null && !isMax) {
					height = newValue.doubleValue();
				}
			}
		});

		gpTitle.setOnMouseMoved((MouseEvent event) -> {
			event.consume();
			double x = event.getSceneX();
			double y = event.getSceneY();
			double width = primaryStage.getWidth();
			double height = primaryStage.getHeight();
			Cursor cursorType = Cursor.DEFAULT;// 鼠标光标初始为默认类型，若未进入调整窗口状态，保持默认类型
			// 先将所有调整窗口状态重置
			isRight = isBottomRight = isBottom = false;
			if (y >= height - RESIZE_WIDTH) {
				if (x <= RESIZE_WIDTH) {// 左下角调整窗口状态
					// 不处理

				} else if (x >= width - RESIZE_WIDTH) {// 右下角调整窗口状态
					isBottomRight = true;
					cursorType = Cursor.SE_RESIZE;
				} else {// 下边界调整窗口状态
					isBottom = true;
					cursorType = Cursor.S_RESIZE;
				}
			} else if (x >= width - RESIZE_WIDTH) {// 右边界调整窗口状态
				isRight = true;
				cursorType = Cursor.E_RESIZE;
			}
			// 最后改变鼠标光标
			gpTitle.setCursor(cursorType);
		});

		gpTitle.setOnMouseDragged((MouseEvent event) -> {
			// 根据鼠标的横纵坐标移动dialog位置
			event.consume();
			if (yOffset != 0) {
				primaryStage.setX(event.getScreenX() - xOffset);
				if (event.getScreenY() - yOffset < 0) {
					primaryStage.setY(0);
				} else {
					primaryStage.setY(event.getScreenY() - yOffset);
				}
			}

			double x = event.getSceneX();
			double y = event.getSceneY();
			// 保存窗口改变后的x、y坐标和宽度、高度，用于预判是否会小于最小宽度、最小高度
			double nextX = primaryStage.getX();
			double nextY = primaryStage.getY();
			double nextWidth = primaryStage.getWidth();
			double nextHeight = primaryStage.getHeight();
			if (isRight || isBottomRight) {// 所有右边调整窗口状态
				nextWidth = x;
			}
			if (isBottomRight || isBottom) {// 所有下边调整窗口状态
				nextHeight = y;
			}
			if (nextWidth <= MIN_WIDTH) {// 如果窗口改变后的宽度小于最小宽度，则宽度调整到最小宽度
				nextWidth = MIN_WIDTH;
			}
			if (nextHeight <= MIN_HEIGHT) {// 如果窗口改变后的高度小于最小高度，则高度调整到最小高度
				nextHeight = MIN_HEIGHT;
			}
			// 最后统一改变窗口的x、y坐标和宽度、高度，可以防止刷新频繁出现的屏闪情况
			primaryStage.setX(nextX);
			primaryStage.setY(nextY);
			primaryStage.setWidth(nextWidth);
			primaryStage.setHeight(nextHeight);

		});
		// 鼠标点击获取横纵坐标
		gpTitle.setOnMousePressed(event -> {
			event.consume();
			xOffset = event.getSceneX();
			if (event.getSceneY() > 46) {
				yOffset = 0;
			} else {
				yOffset = event.getSceneY();
			}
		});
		gpTitle.setOnMouseMoved(event -> {
			event.consume();
			if (event.getSceneY() > 46) {
				gpTitle.getStyleClass().removeAll("sursor-move");
			} else {
				gpTitle.getStyleClass().add("sursor-move");
			}
		});

		return gpTitle;
	}

	// 初始化时刻大表
	private void initialDataAndUI(MainController controller) throws IOException {
		Thread initialData = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 生成时刻表VM
				try {
					timeTableVM = new TimeTableVM();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		initialData.start();
		try {
			initialData.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 主界面控制器设置
		controller.setData(timeTableVM);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
