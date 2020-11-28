import React, { useState, useEffect  } from 'react';
import Paper from '@material-ui/core/Paper';
import { makeStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import {TableRow} from "@material-ui/core";
import {TableCell} from "@material-ui/core";
import {Table} from "@material-ui/core";
import ButtonGroup from '@material-ui/core/ButtonGroup';
import { Link } from 'react-router-dom';
import AuthService from "../../../Service/AuthService";
import NewGenre from "./Genre/NewGenre";


const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
        overflow: 'hidden',
        padding: theme.spacing(0, 3),
    },
    paper: {
        maxWidth: 500,
        marginTop:"2%",
        margin: `${theme.spacing(1)}px auto`,
        padding: theme.spacing(2),
    },
}));



export default function AutoGridNoWrap(props) {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <Button
                component={ Link } to="/home"
                style={{marginLeft:"47%",marginTop:"2%",backgroundColor:"#E5E7E9"}}
            >Home</Button>
            <Paper className={classes.paper}>
                <Grid container wrap="nowrap" spacing={2}>
                    <Grid item xs>
                        <Table >
                            <TableCell>{AuthService.getCurrentUserName()}</TableCell>
                            <TableCell>
                                <Button
                                    onClick={()=>{AuthService.logout();
                                        props.history.push("/");
                                        window.location.reload();}}
                                    style={{marginLeft:"50%",backgroundColor:"#D7BDE2"}}>Logout</Button></TableCell>
                        </Table>
                        <Table>
                            <TableRow><Button orientation="vertical"
                                              color="primary"
                                              aria-label="vertical contained primary button group"
                                              variant="contained"
                                              style={{textAlign:"center",marginTop:10,marginLeft:"32%"}}
                                              component={Link} to="/admin-book-new">Add new Book</Button></TableRow>
                            <TableRow><Button
                                orientation="vertical"
                                color="primary"
                                aria-label="vertical contained primary button group"
                                variant="contained"
                                style={{textAlign:"center",marginTop:10,marginLeft:"30%"}}
                                component={Link} to={"/admin-unverified"}>Unverified Book</Button>
                            </TableRow>

                            <Button
                                orientation="vertical"
                                color="primary"
                                aria-label="vertical contained primary button group"
                                variant="contained"
                                style={{textAlign:"center",marginTop:10,marginLeft:"30%"}}
                                component={Link} to={"/admin-author-new" }>Add new Author</Button>
                            <TableRow><Button
                                orientation="vertical"
                                color="primary"
                                aria-label="vertical contained primary button group"
                                variant="contained"
                                style={{textAlign:"center",marginTop:10,marginLeft:"38%"}}
                                component={Link} to={"/admin-genres"}>Genres</Button>
                            </TableRow>
                        </Table>
                    </Grid>
                </Grid>
            </Paper>
        </div>
    );
}
