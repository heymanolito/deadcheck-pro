package com.example.application.ui.users;

import com.example.application.backend.data.entity.Department;
import com.example.application.backend.data.entity.Status;
import com.example.application.backend.data.entity.User;
import com.example.application.backend.data.service.UserService;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class NewEmployeeView extends VerticalLayout implements KeyNotifier {

    //Servicio
    private final UserService userService;

    //Usuario a editar
    private User user;

    //Campos de texto
    private final TextField profilePicture = new TextField("Foto de perfil");
    private final TextField firstName = new TextField("Nombre");
    private final TextField lastName = new TextField("Apellidos");
    private final TextField email = new TextField("Email");
    private final ComboBox<Department> departmentComboBox = new ComboBox<>("Departamento", Department.values());
    private final ComboBox<Status> statusComboBox = new ComboBox<>("Estado", Status.values());

    //Botones
    Button save = new Button("Guardar", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancelar");
    Button delete = new Button("Borrar", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<User> binder = new Binder<>(User.class);
    //private ChangeHandler changeHandler;


    @Autowired
    public NewEmployeeView(UserService userService) {
        this.userService = userService;

        add(profilePicture, firstName, lastName, email, departmentComboBox, statusComboBox);
        binder.bindInstanceFields(this);

    }

//    void delete() {
//        userService.delete(user.getUser_id());
//        changeHandler.onChange();
//    }
//
//    void save() {
//        userService.update(user);
//        changeHandler.onChange();
//    }
//
//    public final void editUser(User u) {
//        if (u == null) {
//            setVisible(false);
//            return;
//        }
//        final boolean persisted = u.getUser_id() != null;
//        if (persisted && userService.findById(u.getUser_id()).isPresent()) {
//            // Find fresh entity for editing
//            user = userService.findById(u.getUser_id()).get();
//        }
//        else {
//            user = u;
//        }
//        cancel.setVisible(persisted);
//
//        binder.setBean(user);
//
//        setVisible(true);
//
//
//        firstName.focus();
//    }
//
//    public interface ChangeHandler {
//        void onChange();
//    }
//
//    public void setChangeHandler(ChangeHandler h) {
//        // ChangeHandler is notified when either save or delete
//        // is clicked
//        changeHandler = h;
//    }

}
