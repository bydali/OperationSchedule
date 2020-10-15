package schedule.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import schedule.model.TimeTable;
import schedule.model.TrainState;
import schedule.viewmodel.TimeTableVM;

public class Write2Local {
	public static void save2Disk(String path,TimeTable timeTable) throws IOException {
		if (path != null) {
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("YXJH");

			for (Map.Entry<String, List<TrainState>> entry : timeTable.allTrainPointTask.entrySet()) {
				Element rwdh = root.addElement("RWDH");
				rwdh.setText(entry.getValue().get(0).rwdh);
				Element lccc = root.addElement("LCCC");
				lccc.addText(entry.getKey());
				Element yxzd = root.addElement("YXZD");
				yxzd.addAttribute("CZSM", entry.getValue().get(0).czsm);

				int size = entry.getValue().size();
				for (int i = 0; i < size; i++) {
					TrainState trainState = entry.getValue().get(i);
					Element cz = yxzd.addElement("CZ");

					cz.addAttribute("CZBH", String.valueOf(trainState.idx));
					cz.addAttribute("CZLB", trainState.czlb);
					cz.addAttribute("CZMZ", trainState.stationName);
					switch (trainState.czlb) {
					case "始发站":
						cz.addAttribute("CZSJ",
								trainState.date.toString() + "  " + String.format("%tT", trainState.time));
						cz.addAttribute("JZSJ", "");
						cz.addAttribute("SSLJ", trainState.sslj);
						cz.addAttribute("TLDS", trainState.track);
						break;
					default:
						if (i != size - 1) {
							cz.addAttribute("CZSJ", entry.getValue().get(i + 1).date.toString() + "  "
									+ String.format("%tT", entry.getValue().get(i + 1).time));
							cz.addAttribute("JZSJ",
									trainState.date.toString() + "  " + String.format("%tT", trainState.time));
							cz.addAttribute("SSLJ", trainState.sslj);
							cz.addAttribute("TLDS", trainState.track);
							i++;
						} else {
							cz.addAttribute("CZSJ", "");
							cz.addAttribute("JZSJ",
									trainState.date.toString() + "  " + String.format("%tT", trainState.time));
							cz.addAttribute("SSLJ", trainState.sslj);
							cz.addAttribute("TLDS", trainState.track);
						}
						break;
					}
				}
			}

			OutputFormat format = OutputFormat.createPrettyPrint();
			// XMLWriter write = new XMLWriter(new FileWriter(new File(path)), format);
			XMLWriter write = new XMLWriter(new FileOutputStream(new File(path)), format);

			write.write(doc);
			write.close();
		}
	}
}
